package kh.roponpov.compose_google_sheets_integration.view.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navigator: NavController,
    userViewModel: UserViewModel,
    memberRegistrationViewModel: MemberRegistrationViewModel,
) {

    val systemUiController = rememberSystemUiController()
    val primaryColor = Color(0xFFF5F5F5)
    val darkIcons = primaryColor.luminance() > 0.5f

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(MemberFilter.All) }

    val isRefreshing by memberRegistrationViewModel.isRefreshing.collectAsState()

    val listState = rememberLazyListState()
    val indicatorState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        memberRegistrationViewModel.getMemberRegistration()
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = darkIcons
        )
    }

    val members by memberRegistrationViewModel
        .memberRegistrations
        .observeAsState(emptyList())

    val isLoading by memberRegistrationViewModel
        .isLoading
        .observeAsState(initial = true)

    val filtered = members
        .sortedByDescending { it.id }
        .filter { it.matchesSearch(searchQuery) }
        .filter { selectedFilter.matches(it) }

    LaunchedEffect(filtered.size) {
        if (filtered.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopBarSection(members.size,userViewModel)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate("add_member")
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Add member",
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchSection(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                onTrailingTap = {
                    searchQuery = ""
                }
            )

            // Filter chips
            FilterRowSection(
                selectedFilter = selectedFilter,
                onFilterChange = { selectedFilter = it }
            )

            // List
            if (isLoading && members.isEmpty()) {
                MemberListShimmer()
            } else {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { memberRegistrationViewModel.refreshMembers() },
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                    state = indicatorState,
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = indicatorState,
                            isRefreshing = isRefreshing,
                            color = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(filtered, key = { it.id }) { member ->
                            MemberCard(
                                member = member,
                                onEdit = {
                                    println(it.id)
                                    navigator.navigate("update/${it.id}")
                                }
                            )
                        }
                    }
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
        ComposeGoogleSheetsIntegrationTheme {
            Scaffold { padding ->
                HomeScreen(padding, rememberNavController(),viewModel(),viewModel())
            }
        }
    }
}