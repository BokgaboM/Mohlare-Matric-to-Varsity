package za.co.mohlare.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import za.co.mohlare.app.data.AuthRepository
import za.co.mohlare.app.data.UserProfile
import za.co.mohlare.app.data.UserRepository
import za.co.mohlare.app.data.toFriendlyMessage

/** Where the learner is in the app: decides which part of the app to show. */
sealed interface AppState {
    data object Loading : AppState
    data object SignedOut : AppState
    data class NeedsProfile(val uid: String, val suggestedName: String) : AppState
    data class Ready(val uid: String, val profile: UserProfile) : AppState
    data class Failed(val message: String) : AppState
}

class AppViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val users: UserRepository = UserRepository(),
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AppState> = auth.currentUser
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(AppState.SignedOut)
            } else {
                users.profile(user.uid)
                    .map { profile ->
                        if (profile == null || !profile.consentAccepted) {
                            AppState.NeedsProfile(user.uid, user.displayName.orEmpty())
                        } else {
                            AppState.Ready(user.uid, profile)
                        }
                    }
                    .catch { emit(AppState.Failed(it.toFriendlyMessage())) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppState.Loading)

    /** True while a sign-in or save is in progress, so buttons can show a spinner. */
    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    /** A message to show the learner, e.g. a wrong password. Cleared with [clearMessage]. */
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun clearMessage() { _message.value = null }
    fun showMessage(text: String) { _message.value = text }

    private fun launchAction(successMessage: String? = null, block: suspend () -> Unit) {
        viewModelScope.launch {
            _busy.value = true
            _message.value = null
            try {
                block()
                if (successMessage != null) _message.value = successMessage
            } catch (e: Exception) {
                _message.value = e.toFriendlyMessage()
            } finally {
                _busy.value = false
            }
        }
    }

    fun signInWithGoogle(idToken: String) = launchAction { auth.signInWithGoogle(idToken) }

    fun signInWithEmail(email: String, password: String) = launchAction { auth.signInWithEmail(email, password) }

    fun registerWithEmail(email: String, password: String) = launchAction { auth.registerWithEmail(email, password) }

    fun sendPasswordReset(email: String) =
        launchAction(successMessage = "Check your email for a link to reset your password.") { auth.sendPasswordReset(email) }

    fun saveProfile(uid: String, profile: UserProfile) = launchAction { users.saveProfile(uid, profile) }

    fun signOut() = auth.signOut()

    /** POPIA and Google Play: learners must be able to delete their account and data. */
    fun deleteAccount(uid: String) = launchAction {
        users.deleteProfile(uid)
        auth.deleteCurrentUser()
    }
}
