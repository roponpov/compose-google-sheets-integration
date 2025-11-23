package kh.roponpov.compose_google_sheets_integration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kh.roponpov.compose_google_sheets_integration.models.GoogleAuthManager
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.view.add.AddMemberScreen
import kh.roponpov.compose_google_sheets_integration.view.home.HomeScreen
import kh.roponpov.compose_google_sheets_integration.view.login.GoogleLoginScreen
import kh.roponpov.compose_google_sheets_integration.view.update.UpdateMemberScreen
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeGoogleSheetsIntegrationTheme {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel()
                val memberRegistrationModel: MemberRegistrationViewModel = viewModel()

                Scaffold { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") {
                            GoogleLoginScreen(
                                userViewModel = userViewModel,
                                onLoginSuccess = { token ->
                                    GoogleAuthManager.accessToken = token
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
                        composable("add_member") { AddMemberScreen(navController) }

                        composable("update/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            if (id != null) {
                                UpdateMemberScreen(
                                    navigator = navController,
                                    memberRegistrationViewModel = memberRegistrationModel,
                                    id = id
                                )
                            } else {
                                // Handle the case where ID is invalid or null
                                Text("Error: Invalid Member ID")
                            }
                        }
                    }
                }
            }
        }
    }
}
