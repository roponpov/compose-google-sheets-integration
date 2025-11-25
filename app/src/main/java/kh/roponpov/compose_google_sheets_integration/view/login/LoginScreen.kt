@file:Suppress("DEPRECATION")

package kh.roponpov.compose_google_sheets_integration.view.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kh.roponpov.compose_google_sheets_integration.BuildConfig
import kh.roponpov.compose_google_sheets_integration.R
import kh.roponpov.compose_google_sheets_integration.view.component.AppErrorDialog
import kh.roponpov.compose_google_sheets_integration.view.component.AppLoadingDialog
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GoogleLoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: (String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isSigningIn by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {
            val account = task.getResult(ApiException::class.java)
            userViewModel.setFromGoogleAccount(
                context = context,
                account = account
            )
            scope.launch {
                isSigningIn = true
                errorMessage = null
                try {
                    val token = withContext(Dispatchers.IO) {
                        GoogleAuthUtil.getToken(
                            context,
                            account.account!!,
                            "oauth2:https://www.googleapis.com/auth/spreadsheets"
                        )
                    }
                    Log.d("GoogleLogin", "accessToken = $token")
                    onLoginSuccess(token)
                } catch (e: Exception) {
                    Log.e("GoogleLogin", "Failed to get access token", e)
                    errorMessage = "Failed to get access token. Please try again."
                } finally {
                    isSigningIn = false
                }
            }
        } catch (e: ApiException) {
            Log.e("GoogleLogin", "Google sign-in failed", e)
            errorMessage = "Google sign-in failed. Please try again."
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // ========= CENTERED CONTENT BLOCK =========
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(R.drawable.app_logo),
                            contentDescription = "App Logo"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Sheet Sync",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Sign in with your Google Account to securely sync data to Google Sheets.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "What you get",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                FeatureRow(
                                    icon = Icons.Default.Refresh,
                                    title = "Instant sync",
                                    subtitle = "Send data straight into your Google Sheets without exporting files."
                                )

                                FeatureRow(
                                    icon = Icons.Default.CheckCircle,
                                    title = "One-tap login",
                                    subtitle = "Use the Google account already on your device. No extra passwords."
                                )

                                FeatureRow(
                                    icon = Icons.Default.Lock,
                                    title = "Scoped access",
                                    subtitle = "We only request Sheets access. You can revoke it anytime."
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        GoogleSignInButton(
                            enabled = !isSigningIn,
                            onClick = {
                                errorMessage = null
                                launchGoogleLogin(context, signInLauncher)
                            }
                        )

                        if (isSigningIn) {
                            AppLoadingDialog(
                                loadingText = "Connecting to your Google Account…"
                            )
                        }

                        // ========= ERROR DIALOG =========
                        if (errorMessage != null) {
                            AppErrorDialog(
                                title = "Sign-in failed",
                                message = errorMessage!!,
                                onDismiss = { errorMessage = null }
                            )
                        }
                    }
                }

                // ========= FOOTER =========
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Developed by Pov Ropon • v1.0.0",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Manage access anytime in your Google Account settings.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun GoogleSignInButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(alignment = Alignment.CenterHorizontally, space = 10.dp)
        ){
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.google_logo_icon),
                contentDescription = "Google Logo"
            )

            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

fun launchGoogleLogin(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, androidx.activity.result.ActivityResult>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(BuildConfig.CLIENT_ID)
        .requestScopes(Scope("https://www.googleapis.com/auth/spreadsheets"))
        .build()

    val client = GoogleSignIn.getClient(context, gso)
    launcher.launch(client.signInIntent)
}
