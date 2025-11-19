package kh.roponpov.compose_google_sheets_integration.view.add_member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
        AppTextField(
            label = "Latin Name",
            value = latinName,
            onValueChange = { latinName = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // Khmer Name
        AppTextField(
            label = "Khmer Name",
            value = khmerName,
            onValueChange = { khmerName = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // Gender
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded },
        ) {
            AppDropdownField(
                label = "Gender",
                value = gender.text,
                expanded = genderExpanded,
                onClick = { genderExpanded = !genderExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                GenderType.entries.forEach { option ->
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
        AppTextField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        // Phone
        AppTextField(
            label = "Phone",
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        // Payment Status
        ExposedDropdownMenuBox(
            expanded = paymentExpanded,
            onExpandedChange = { paymentExpanded = !paymentExpanded },
        ) {
            AppDropdownField(
                label = "Payment Status",
                value = paymentStatus.text,
                expanded = paymentExpanded,
                onClick = { paymentExpanded = !paymentExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = paymentExpanded,
                onDismissRequest = { paymentExpanded = false }
            ) {
                PaymentStatus.entries.forEach { option ->
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

        // Address (multiline)
        AppTextArea(
            label = "Address",
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
        )

        // DOB
        AppTextField(
            label = "Date of Birth (dd/MM/yyyy)",
            value = dob,
            onValueChange = { dob = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // Registration Date
        AppTextField(
            label = "Registration Date (dd/MM/yyyy)",
            value = registrationDate,
            onValueChange = { registrationDate = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // Degree
        ExposedDropdownMenuBox(
            expanded = degreeExpanded,
            onExpandedChange = { degreeExpanded = !degreeExpanded },
        ) {
            AppDropdownField(
                label = "Degree",
                value = degree.text,
                expanded = degreeExpanded,
                onClick = { degreeExpanded = !degreeExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = degreeExpanded,
                onDismissRequest = { degreeExpanded = false }
            ) {
                DegreeType.entries.forEach { option ->
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

        // Remark (multiline)
        AppTextArea(
            label = "Remark",
            value = remark,
            onValueChange = { remark = it },
            modifier = Modifier.fillMaxWidth(),
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
                        id = 0,
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
    Row(
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
    Row(
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
