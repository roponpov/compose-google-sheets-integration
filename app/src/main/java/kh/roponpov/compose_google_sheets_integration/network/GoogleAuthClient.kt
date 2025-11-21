package kh.roponpov.compose_google_sheets_integration.network

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleAuthClient(
    private val context: Context
) {
    private val signInClient = Identity.getSignInClient(context)

    suspend fun signIn(): String {
        return suspendCancellableCoroutine { cont ->

            val request = BeginSignInRequest.Builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("1086370985086-c8ajmukf5pnbngmk4uq5fj93vcukec9p.apps.googleusercontent.com")
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(false)
                .build()

            signInClient.beginSignIn(request)
                .addOnSuccessListener { result ->
                    val intentSender = result.pendingIntent.intentSender
                    cont.resume(intentSender.toString())
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
    }
}
