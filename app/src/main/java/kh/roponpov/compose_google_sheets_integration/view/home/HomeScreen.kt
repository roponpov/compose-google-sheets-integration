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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kh.roponpov.compose_google_sheets_integration.ui.theme.ComposeGoogleSheetsIntegrationTheme
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(paddingValues: PaddingValues,navigator: NavController) {

    val systemUiController = rememberSystemUiController()
    val primaryColor = Color(0xFFF5F5F5)
    val darkIcons = primaryColor.luminance() > 0.5f

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(MemberFilter.All) }
    val memberRegistrationViewModel: MemberRegistrationViewModel = viewModel()
    memberRegistrationViewModel.getMemberRegistration()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = darkIcons
        )
    }

    val members by memberRegistrationViewModel
        .memberRegistrations
        .observeAsState(emptyList())

    val filtered = members
        .filter { it.matchesSearch(searchQuery) }
        .filter { selectedFilter.matches(it) }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopBarSection(members.size)
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

            // Search
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                value = searchQuery,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
                onValueChange = { searchQuery = it },
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
                shape = RoundedCornerShape(10.dp),
            )

            // Filter chips
            FilterRowSection(
                selectedFilter = selectedFilter,
                onFilterChange = { selectedFilter = it }
            )

            // List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
        ComposeGoogleSheetsIntegrationTheme {
            Scaffold { padding ->
                HomeScreen(padding, rememberNavController())
            }
        }
    }
}