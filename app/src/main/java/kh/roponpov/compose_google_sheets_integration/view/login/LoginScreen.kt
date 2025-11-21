package kh.roponpov.compose_google_sheets_integration.view.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GoogleLoginScreen(
    onLoginSuccess: (String) -> Unit,   // <-- accessToken
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {
            val account = task.getResult(ApiException::class.java)

            scope.launch {
                try {
                    val token = withContext(Dispatchers.IO) {
                        GoogleAuthUtil.getToken(
                            context,
                            account.account!!, // Google account on device
                            "oauth2:https://www.googleapis.com/auth/spreadsheets"
                        )
                    }
                    Log.d("GoogleLogin", "accessToken = $token")
                    onLoginSuccess(token)
                } catch (e: Exception) {
                    Log.e("GoogleLogin", "Failed to get access token", e)
                }
            }
        } catch (e: ApiException) {
            Log.e("GoogleLogin", "Google sign-in failed", e)
        }
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                launchGoogleLogin(context, signInLauncher)
            }
        ) {
            Text("Sign in with Google")
        }
    }
}

fun launchGoogleLogin(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, androidx.activity.result.ActivityResult>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        // 👉 WEB CLIENT ID here (already fixed earlier)
        .requestIdToken("1086370985086-uf38gh99egd49kd2l7a4kac6nim55r84.apps.googleusercontent.com")
        .requestScopes(Scope("https://www.googleapis.com/auth/spreadsheets"))
        .build()

    val client = GoogleSignIn.getClient(context, gso)
    launcher.launch(client.signInIntent)
}
