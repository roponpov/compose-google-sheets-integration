package kh.roponpov.compose_google_sheets_integration

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kh.roponpov.compose_google_sheets_integration.models.GoogleAuthManagerModel
import kh.roponpov.compose_google_sheets_integration.models.LanguageManager
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.view.add.AddMemberScreen
import kh.roponpov.compose_google_sheets_integration.view.home.HomeScreen
import kh.roponpov.compose_google_sheets_integration.view.language.LanguageScreen
import kh.roponpov.compose_google_sheets_integration.view.login.GoogleLoginScreen
import kh.roponpov.compose_google_sheets_integration.view.splash.SplashScreen
import kh.roponpov.compose_google_sheets_integration.view.update.UpdateMemberScreen
import kh.roponpov.compose_google_sheets_integration.viewmodel.AppStartupViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    private val appStartupViewModel: AppStartupViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ComposeGoogleSheetsIntegrationTheme {
                val isLoading = appStartupViewModel
                    .isLoading
                    .collectAsStateWithLifecycle()
                    .value

                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel()
                val memberRegistrationModel: MemberRegistrationViewModel = viewModel()
//                SplashScreen()
                if(isLoading) {
                    SplashScreen()
                } else {
                    Scaffold { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = "language",
                        ) {
                            composable("language") {
                                // remember selected language locally in this screen
                                var selectedLanguage by rememberSaveable {
                                    mutableStateOf(
                                        LanguageManager.getSavedLanguage(this@MainActivity)
                                    )
                                }

                                LanguageScreen(
                                    selectedLanguage = selectedLanguage,
                                    onLanguageSelected = { lang ->
                                        selectedLanguage = lang
                                    },
                                    onConfirm = {
                                        // apply + recreate activity (inside LanguageManager)
                                        LanguageManager.updateAppLocale(
                                            this@MainActivity,
                                            selectedLanguage
                                        )
                                        // after language pick, go to login
                                        navController.navigate("login") {
                                            popUpTo("language") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("login") {
                                GoogleLoginScreen(
                                    userViewModel = userViewModel,
                                    onLoginSuccess = { token ->
                                        GoogleAuthManagerModel.accessToken = token
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    })
                            }

                            composable("home") {
                                HomeScreen(
                                    paddingValues = padding,
                                    navigator = navController,
                                    userViewModel = userViewModel,
                                    memberRegistrationViewModel = memberRegistrationModel
                                )
                            }
                            composable("add") { AddMemberScreen(navController) }

                            composable("update/{id}") { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                                if (id != null) {
                                    UpdateMemberScreen(
                                        navigator = navController,
                                        memberRegistrationViewModel = memberRegistrationModel,
                                        id = id
                                    )
                                } else {
                                    Text("Error: Invalid Member ID")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
