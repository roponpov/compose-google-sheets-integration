package kh.roponpov.compose_google_sheets_integration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kh.roponpov.compose_google_sheets_integration.models.GoogleAuthManager
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.view.add_member.AddMemberScreen
import kh.roponpov.compose_google_sheets_integration.view.home.HomeScreen
import kh.roponpov.compose_google_sheets_integration.view.login.GoogleLoginScreen
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeGoogleSheetsIntegrationTheme {
                val navController = rememberNavController()
                val userViewMode: UserViewModel = viewModel()

                Scaffold { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") {
                            GoogleLoginScreen(
                                userViewModel = userViewMode,
                                onLoginSuccess = { token ->
                                    GoogleAuthManager.accessToken = token
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                })
                        }

                        composable("home") { HomeScreen(padding, navController, userViewMode) }
                        composable("add_member") { AddMemberScreen(navController) }
                    }
                }
            }
        }
    }
}
