package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kh.roponpov.compose_google_sheets_integration.R
import kh.roponpov.compose_google_sheets_integration.core.prefs.AppPreferences
import kh.roponpov.compose_google_sheets_integration.models.MemberFilter
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.view.component.AppConfirmDialog
import kh.roponpov.compose_google_sheets_integration.view.component.AppCustomDialog
import kh.roponpov.compose_google_sheets_integration.view.component.AppLoadingDialog
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    padding: PaddingValues,
    navigator: NavController,
    userViewModel: UserViewModel,
    memberRegistrationViewModel: MemberRegistrationViewModel,
) {
    val context = LocalContext.current
    val accessToken = AppPreferences.getAccessToken(context)

    val systemUiController = rememberSystemUiController()

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
            color = Color.Transparent,
            darkIcons = false
        )
    }

    val members by memberRegistrationViewModel
        .memberRegistrations
        .observeAsState(emptyList())

    val isLoading by memberRegistrationViewModel
        .isLoading
        .observeAsState(initial = true)

    val isSubmitting by memberRegistrationViewModel
        .isSubmitting
        .observeAsState(initial = false)

    val deleteResult by memberRegistrationViewModel
        .deleteResult
        .observeAsState()

    var memberToDelete by remember { mutableStateOf<MemberRegistrationModel?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

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
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopBarSection(members.size,userViewModel,navigator)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate("add")
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
                                    navigator.navigate("update/${it.id}")
                                },
                                onDelete = { clickedMember ->
                                    memberToDelete = clickedMember
                                    showDeleteConfirm = true

                                }
                            )
                        }
                    }
                }
            }
        }

        if (isSubmitting) {
            AppLoadingDialog(
                loadingText = stringResource(R.string.deleting_member)
            )
        }

        if (showDeleteConfirm && memberToDelete != null) {
            val name = memberToDelete!!.latinName

            AppConfirmDialog(
                title = stringResource(R.string.remove_this_member),
                message = "“$name” ${stringResource(R.string.delete_member_message)}",
                highlightedText = name,
                confirmText = stringResource(R.string.delete),
                cancelText = stringResource(R.string.keep),
                isDestructive = true,
                onConfirm = {
                    showDeleteConfirm = false
                    accessToken?.let {
                        memberRegistrationViewModel.deleteMember(
                            accessToken = it,
                            member = memberToDelete!!
                        )
                    }
                    memberToDelete = null
                },
                onCancel = {
                    showDeleteConfirm = false
                    memberToDelete = null
                }
            )
        }

        deleteResult?.let { result ->
            AppCustomDialog(
                title = if (result is MemberRegistrationViewModel.SubmitResult.Success)
                    stringResource(R.string.member_deleted)
                else
                    stringResource(R.string.delete_failed),
                description = if (result is MemberRegistrationViewModel.SubmitResult.Success)
                    stringResource(R.string.the_member_has_been_deleted_successfully)
                else
                    "",
                result = result,
                onDismiss = {
                    memberRegistrationViewModel.clearDeleteResult()
                },
                onSuccessNavigateBack = {
                    memberRegistrationViewModel.clearDeleteResult()
                }
            )
        }
    }
}