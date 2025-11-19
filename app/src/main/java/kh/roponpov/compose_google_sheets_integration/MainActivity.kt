package kh.roponpov.compose_google_sheets_integration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.view.add_member.AddMemberScreen
import kh.roponpov.compose_google_sheets_integration.view.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeGoogleSheetsIntegrationTheme {
                Scaffold { padding ->

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                    ) {
                        composable("home") {
                            HomeScreen(padding,navController)
                        }

                        composable("add_member") {
                            AddMemberScreen(navController)
                        }
                    }
                }
            }
        }
    }
}