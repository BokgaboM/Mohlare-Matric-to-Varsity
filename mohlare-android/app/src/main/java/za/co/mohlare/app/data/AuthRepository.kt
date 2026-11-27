package za.co.mohlare.app.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/** Everything to do with signing in and out, through Firebase Authentication. */
class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    /** Emits the signed-in user, or null when nobody is signed in. */
    val currentUser: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    suspend fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }

    suspend fun registerWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email.trim()).await()
    }

    fun signOut() = auth.signOut()

    /** Deletes the sign-in account itself. Firebase may ask the learner to sign in again first. */
    suspend fun deleteCurrentUser() {
        auth.currentUser?.delete()?.await()
    }
}

/** Turns Firebase errors into short messages a learner can act on. */
fun Throwable.toFriendlyMessage(): String = when (this) {
    is FirebaseNetworkException -> "No internet connection. Check your data or Wi-Fi and try again."
    is FirebaseAuthWeakPasswordException -> "Use a password with at least 6 characters."
    is FirebaseAuthUserCollisionException -> "There's already an account with this email. Sign in instead."
    is FirebaseAuthInvalidUserException -> "We couldn't find an account with that email."
    is FirebaseAuthInvalidCredentialsException -> "That email or password isn't right."
    is FirebaseAuthRecentLoginRequiredException -> "For your safety, sign out, sign in again, then try once more."
    else -> "Something went wrong. Please try again."
}
