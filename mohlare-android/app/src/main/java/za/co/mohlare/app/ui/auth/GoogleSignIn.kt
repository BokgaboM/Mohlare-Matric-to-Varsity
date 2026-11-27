package za.co.mohlare.app.ui.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import za.co.mohlare.app.R

/** What happened when the learner tapped "Continue with Google". */
sealed interface GoogleResult {
    data class Success(val idToken: String) : GoogleResult
    data object Cancelled : GoogleResult
    data class Error(val message: String) : GoogleResult
}

/**
 * Shows Google's account picker and returns an ID token for Firebase.
 * [context] must be the Activity (LocalContext in Compose is fine).
 *
 * `default_web_client_id` is created automatically from google-services.json,
 * once Google is turned on as a sign-in method in the Firebase console.
 */
suspend fun requestGoogleIdToken(context: Context): GoogleResult {
    val option = GetSignInWithGoogleOption.Builder(
        context.getString(R.string.default_web_client_id)
    ).build()
    val request = GetCredentialRequest.Builder().addCredentialOption(option).build()

    return try {
        val result = CredentialManager.create(context).getCredential(context, request)
        val credential = result.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            GoogleResult.Success(GoogleIdTokenCredential.createFrom(credential.data).idToken)
        } else {
            GoogleResult.Error("Google sign-in didn't work. Try email instead.")
        }
    } catch (e: GetCredentialCancellationException) {
        GoogleResult.Cancelled
    } catch (e: GetCredentialException) {
        GoogleResult.Error("Google sign-in didn't work on this phone. Try email instead.")
    }
}

/** Forget the chosen Google account, so the picker shows again next time. */
suspend fun clearGoogleSignIn(context: Context) {
    try {
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    } catch (_: Exception) {
        // Nothing to clear; safe to ignore.
    }
}
