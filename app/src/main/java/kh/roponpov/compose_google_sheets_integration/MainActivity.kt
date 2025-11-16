package kh.roponpov.compose_google_sheets_integration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kh.roponpov.compose_google_sheets_integration.view.add_data.AddDataScreen
import kh.roponpov.compose_google_sheets_integration.view.bottom_navigation_bar.BottomNavItem
import kh.roponpov.compose_google_sheets_integration.view.bottom_navigation_bar.BottomNavigationBar
import kh.roponpov.compose_google_sheets_integration.view.home.HomeScreen
import kh.roponpov.compose_google_sheets_integration.view.profile.ProfileScreen
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeGoogleSheetsIntegrationTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(BottomNavItem.Home.route) { HomeScreen() }
                        composable(BottomNavItem.Search.route) { AddDataScreen() }
                        composable(BottomNavItem.Profile.route) { ProfileScreen() }
                    }
                }
            }
        }
    }
}