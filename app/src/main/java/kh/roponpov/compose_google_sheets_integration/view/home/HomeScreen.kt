package kh.roponpov.compose_google_sheets_integration.view.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.view.add_data.AddDataScreen
import kh.roponpov.compose_google_sheets_integration.view.bottom_navigation_bar.BottomNavItem
import kh.roponpov.compose_google_sheets_integration.view.bottom_navigation_bar.BottomNavigationBar
import kh.roponpov.compose_google_sheets_integration.view.profile.ProfileScreen
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/*
members: List<MemberRegistrationModel>,
onMemberClick: (MemberRegistrationModel) -> Unit = {},
onAddClick: () -> Unit = {}
* */
fun HomeScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(MemberFilter.All) }
    val memberRegistrationViewModel: MemberRegistrationViewModel = viewModel()
    memberRegistrationViewModel.getMemberRegistration()
    val members by memberRegistrationViewModel
        .memberRegistrations
        .observeAsState(emptyList())

    val filtered = members
        .filter { it.matchesSearch(searchQuery) }
        .filter { selectedFilter.matches(it) }

    Scaffold(
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Column {
                    Text("Members", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "${members.size} records",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                AnimatedProfileRing(
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNRRPTo_P2i0IKquqdmkZ-3KJbRw2GKHUn2w&s",
                    size = 45.dp,
                    ringWidth = 1.dp
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*onAddClick*/ }) {
                Icon(Icons.Default.Add, contentDescription = "Add member")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                placeholder = {
                    Text(
                        text = "Search by name, email, phone...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray,
                        )
                    )
                },

                prefix = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = Color.Gray,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(20.dp)
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 13.sp
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Filter chips
            FilterRowSection(
                selectedFilter = selectedFilter,
                onFilterChange = { selectedFilter = it }
            )

            // List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                println(filtered)
                items(filtered, key = { it.id }) { member ->
                    MemberCard(
                        member = member,
                    )
                }
            }
        }
    }
}


@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Light Mode"
)
fun PreviewMyJetpack(){
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