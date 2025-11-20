package kh.roponpov.compose_google_sheets_integration.view.add_member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color(0xFFF5F5F5),
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
    var gender by rememberSaveable { mutableStateOf<GenderType?>(null) }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var paymentStatus by rememberSaveable { mutableStateOf<PaymentStatus?>(null) }
    var address by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var registrationDate by rememberSaveable { mutableStateOf("") }
    var degree by rememberSaveable { mutableStateOf<DegreeType?>(null) }
    var joinGroup by rememberSaveable { mutableStateOf(false) }
    var remark by rememberSaveable { mutableStateOf("") }

    // DROPDOWN EXPAND STATES
    var genderExpanded by rememberSaveable { mutableStateOf(false) }
    var paymentExpanded by rememberSaveable { mutableStateOf(false) }
    var degreeExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // LATIN NAME
        AppTextField(
            label = "Latin Name",
            value = latinName,
            placeholder = "Enter your latin name",
            onValueChange = { latinName = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // KHMER NAME
        AppTextField(
            label = "Khmer Name",
            value = khmerName,
            placeholder = "Enter your khmer name",
            onValueChange = { khmerName = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
                value = gender?.text ?: "Select gender",
                valueStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, color = if (gender == null) Color.Gray else MaterialTheme.colorScheme.primary),
                expanded = genderExpanded,
                onClick = { genderExpanded = !genderExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false },
                containerColor = MaterialTheme.colorScheme.onPrimary
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
            placeholder = "Enter your email",
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        // Phone
        AppTextField(
            label = "Phone",
            value = phone,
            placeholder = "Enter your phone number",
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
                value = paymentStatus?.text ?: "Select payment status",
                MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, color = if (paymentStatus == null) Color.Gray else MaterialTheme.colorScheme.primary),
                expanded = paymentExpanded,
                onClick = { paymentExpanded = !paymentExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = paymentExpanded,
                onDismissRequest = { paymentExpanded = false },
                containerColor = MaterialTheme.colorScheme.onPrimary
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
            placeholder = "Enter the address",
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        )

        // DOB
        AppTextField(
            label = "Date of Birth",
            value = dob,
            placeholder = "Select your date of birth",
            onValueChange = { dob = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // Registration Date
        AppTextField(
            label = "Registration Date",
            value = registrationDate,
            placeholder = "Enter your registration date",
            onValueChange = { registrationDate = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
                value = degree?.text ?: "Select degree",
                MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, color = if (degree == null) Color.Gray else MaterialTheme.colorScheme.primary),
                expanded = degreeExpanded,
                onClick = { degreeExpanded = !degreeExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = degreeExpanded,
                onDismissRequest = { degreeExpanded = false },
                containerColor = MaterialTheme.colorScheme.onPrimary
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

        SettingSwitchRow(
            title = "Join Group",
            subtitle = "Allow this user to join the group",
            checked = joinGroup,
            onCheckedChange = { joinGroup = it }
        )

        // Remark (multiline)
        AppTextArea(
            label = "Remark",
            placeholder = "Enter the remark",
            value = remark,
            onValueChange = { remark = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
                        gender = gender ?: GenderType.OTHER,
                        email = email,
                        phone = phone,
                        paymentStatus = paymentStatus ?: PaymentStatus.UNPAID,
                        address = address,
                        dob = dob,
                        registrationDate = registrationDate,
                        degree = degree ?: DegreeType.UNKNOWN,
                        joinGroup = joinGroup,
                        remark = remark
                    )
                    onSubmit(member)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.W600
                )
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }
        }

        Switch(
            modifier = Modifier.scale(0.8f),
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

@Composable
@Preview
fun PreviewAddMember(){
    AddMemberScreen(rememberNavController())
}