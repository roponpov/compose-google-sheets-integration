package kh.roponpov.compose_google_sheets_integration.view.add_member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kh.roponpov.compose_google_sheets_integration.models.DegreeType
import kh.roponpov.compose_google_sheets_integration.models.GenderType
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.models.PaymentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    navigator: NavController,
) {
    val systemUiController = rememberSystemUiController()
    val primaryColor = MaterialTheme.colorScheme.primary
    val darkIcons = primaryColor.luminance() > 0.5f

    SideEffect {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = darkIcons
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(
                        "Add new member",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        AddMemberForm(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onSubmit = { member ->
                // TODO: Call your API here and post `member`
                // e.g. viewModel.submitMember(member)
            },
            onCancel = {
                navigator.popBackStack()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMemberForm(
    modifier: Modifier = Modifier,
    onSubmit: (MemberRegistrationModel) -> Unit,
    onCancel: () -> Unit,
) {
    // === FORM STATES ===
    var latinName by rememberSaveable { mutableStateOf("") }
    var khmerName by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf(GenderType.MALE) }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var paymentStatus by rememberSaveable { mutableStateOf(PaymentStatus.UNPAID) }
    var address by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var registrationDate by rememberSaveable { mutableStateOf("") }
    var degree by rememberSaveable { mutableStateOf(DegreeType.BACHELOR) }
    var joinGroup by rememberSaveable { mutableStateOf(false) }
    var remark by rememberSaveable { mutableStateOf("") }

    // Dropdown expand states
    var genderExpanded by rememberSaveable { mutableStateOf(false) }
    var paymentExpanded by rememberSaveable { mutableStateOf(false) }
    var degreeExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Member Information",
            style = MaterialTheme.typography.titleMedium
        )

        // Latin Name
        OutlinedTextField(
            value = latinName,
            onValueChange = { latinName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Latin Name") },
            singleLine = true
        )

        // Khmer Name
        OutlinedTextField(
            value = khmerName,
            onValueChange = { khmerName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Khmer Name") },
            singleLine = true
        )

        // Gender
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = gender.text,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                GenderType.values().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.text) },
                        onClick = {
                            gender = option
                            genderExpanded = false
                        }
                    )
                }
            }
        }

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        // Phone
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone") },
            singleLine = true
        )

        // Payment Status
        ExposedDropdownMenuBox(
            expanded = paymentExpanded,
            onExpandedChange = { paymentExpanded = !paymentExpanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = paymentStatus.text,
                onValueChange = {},
                readOnly = true,
                label = { Text("Payment Status") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = paymentExpanded,
                onDismissRequest = { paymentExpanded = false }
            ) {
                PaymentStatus.values().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.text) },
                        onClick = {
                            paymentStatus = option
                            paymentExpanded = false
                        }
                    )
                }
            }
        }

        // Address
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Address") },
            minLines = 2,
            maxLines = 3
        )

        // DOB (you can replace with real date picker later)
        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Date of Birth (dd/MM/yyyy)") },
            singleLine = true
        )

        // Registration Date
        OutlinedTextField(
            value = registrationDate,
            onValueChange = { registrationDate = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Registration Date (dd/MM/yyyy)") },
            singleLine = true
        )

        // Degree
        ExposedDropdownMenuBox(
            expanded = degreeExpanded,
            onExpandedChange = { degreeExpanded = !degreeExpanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = degree.text,
                onValueChange = {},
                readOnly = true,
                label = { Text("Degree") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = degreeExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = degreeExpanded,
                onDismissRequest = { degreeExpanded = false }
            ) {
                DegreeType.values().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.text) },
                        onClick = {
                            degree = option
                            degreeExpanded = false
                        }
                    )
                }
            }
        }

        // Joined Group
        RowWithSwitch(
            title = "Joined Group",
            checked = joinGroup,
            onCheckedChange = { joinGroup = it }
        )

        // Remark
        OutlinedTextField(
            value = remark,
            onValueChange = { remark = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Remark") },
            minLines = 2,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            RowWithActions(
                onCancel = onCancel,
                onSubmit = {
                    val member = MemberRegistrationModel(
                        id = 0, // or generate locally if needed
                        latinName = latinName,
                        khmerName = khmerName,
                        gender = gender,
                        email = email,
                        phone = phone,
                        paymentStatus = paymentStatus,
                        address = address,
                        dob = dob,
                        registrationDate = registrationDate,
                        degree = degree,
                        joinGroup = joinGroup,
                        remark = remark
                    )
                    onSubmit(member)
                }
            )
        }
    }
}

@Composable
private fun RowWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun RowWithActions(
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
    ) {
        TextButton(onClick = onCancel) {
            Text("Cancel")
        }
        Button(onClick = onSubmit) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMemberScreenPreview() {
    val navController = rememberNavController()
    AddMemberScreen(navigator = navController)
}


@Composable
@Preview
fun PreviewAddMemberScreen() {
    AddMemberScreen(rememberNavController())
}