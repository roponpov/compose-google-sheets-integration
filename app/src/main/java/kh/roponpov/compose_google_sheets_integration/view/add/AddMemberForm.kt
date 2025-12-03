package kh.roponpov.compose_google_sheets_integration.view.add

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kh.roponpov.compose_google_sheets_integration.R
import kh.roponpov.compose_google_sheets_integration.models.DegreeType
import kh.roponpov.compose_google_sheets_integration.models.GenderType
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.models.PaymentStatus
import kh.roponpov.compose_google_sheets_integration.view.component.AppCustomDialog
import kh.roponpov.compose_google_sheets_integration.view.component.AppDatePickerTextField
import kh.roponpov.compose_google_sheets_integration.view.component.AppDropdownField
import kh.roponpov.compose_google_sheets_integration.view.component.AppLoadingDialog
import kh.roponpov.compose_google_sheets_integration.view.component.AppSettingSwitchRow
import kh.roponpov.compose_google_sheets_integration.view.component.AppTextArea
import kh.roponpov.compose_google_sheets_integration.view.component.AppTextField
import kh.roponpov.compose_google_sheets_integration.view.component.toApiDate
import kh.roponpov.compose_google_sheets_integration.viewmodel.MemberRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberForm(
    modifier: Modifier = Modifier,
    navigator: NavController,
    onSubmit: (MemberRegistrationModel) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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

    // === ERROR STATES ===
    var latinNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var khmerNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var genderError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var phoneError by rememberSaveable { mutableStateOf<String?>(null) }
    var paymentStatusError by rememberSaveable { mutableStateOf<String?>(null) }
    var addressError by rememberSaveable { mutableStateOf<String?>(null) }
    var dobError by rememberSaveable { mutableStateOf<String?>(null) }
    var registrationDateError by rememberSaveable { mutableStateOf<String?>(null) }
    var degreeError by rememberSaveable { mutableStateOf<String?>(null) }

    // DROPDOWN EXPAND STATES
    var genderExpanded by rememberSaveable { mutableStateOf(false) }
    var paymentExpanded by rememberSaveable { mutableStateOf(false) }
    var degreeExpanded by rememberSaveable { mutableStateOf(false) }

    // dialogs state
    val memberRegistrationViewModel: MemberRegistrationViewModel = viewModel()
    val isSubmitting by memberRegistrationViewModel.isSubmitting.observeAsState(false)
    val submitResult by memberRegistrationViewModel.submitResult.observeAsState()


    Column (
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ){
        Column(
            modifier = modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // LATIN NAME
            AppTextField(
                label = stringResource(R.string.latin_name),
                value = latinName,
                placeholder = stringResource(R.string.enter_your_latin_name),
                onValueChange = { latinName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                isError = latinNameError != null,
                errorText = latinNameError
            )

            // KHMER NAME
            AppTextField(
                label = stringResource(R.string.khmer_name),
                value = khmerName,
                placeholder = stringResource(R.string.enter_your_khmer_name),
                onValueChange = { khmerName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                isError = khmerNameError != null,
                errorText = khmerNameError
            )

            // Gender
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded },
            ) {
                AppDropdownField(
                    label = stringResource(R.string.gender),
                    value = gender?.text ?: stringResource(R.string.select_gender),
                    valueStyle = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = if (gender == null) Color.Gray else MaterialTheme.colorScheme.onBackground
                    ),
                    expanded = genderExpanded,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    isError = genderError != null,
                    errorText = genderError
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
                label = stringResource(R.string.email),
                value = email,
                placeholder = stringResource(R.string.enter_your_email),
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = emailError != null,
                errorText = emailError
            )

            // Phone
            AppTextField(
                label = stringResource(R.string.phone),
                value = phone,
                placeholder = stringResource(R.string.enter_your_phone_number),
                onValueChange = { newValue ->
                    phone = newValue.filter { it.isDigit() }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = phoneError != null,
                errorText = phoneError
            )

            // Payment Status
            ExposedDropdownMenuBox(
                expanded = paymentExpanded,
                onExpandedChange = { paymentExpanded = !paymentExpanded },
            ) {
                AppDropdownField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    label = stringResource(R.string.payment_status),
                    value = paymentStatus?.text ?: stringResource(R.string.select_payment_status),
                    valueStyle = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = if (paymentStatus == null) Color.Gray else MaterialTheme.colorScheme.onBackground
                    ),
                    expanded = paymentExpanded,
                    isError = paymentStatusError != null,
                    errorText = paymentStatusError
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
                label = stringResource(R.string.address),
                placeholder = stringResource(R.string.enter_the_address),
                value = address,
                onValueChange = { address = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                isError = addressError != null,
                errorText = addressError
            )

            // Date of Birth
            AppDatePickerTextField(
                label = stringResource(R.string.date_of_birth),
                value = dob,
                placeholder = stringResource(R.string.select_your_date_of_birth),
                onDobChange = { dob = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                isError = dobError != null,
                errorText = dobError
            )

            // Registration Date
            AppDatePickerTextField(
                label = stringResource(R.string.registration_date),
                value = registrationDate,
                placeholder = stringResource(R.string.enter_your_registration_date),
                onDobChange = { registrationDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                isError = registrationDateError != null,
                errorText = registrationDateError
            )

            // Degree
            ExposedDropdownMenuBox(
                expanded = degreeExpanded,
                onExpandedChange = { degreeExpanded = !degreeExpanded },
            ) {
                AppDropdownField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    label = stringResource(R.string.degree),
                    value = degree?.text ?: stringResource(R.string.select_degree),
                    valueStyle = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = if (degree == null) Color.Gray else MaterialTheme.colorScheme.onBackground
                    ),
                    expanded = degreeExpanded,
                    isError = degreeError != null,
                    errorText = degreeError
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

            AppSettingSwitchRow(
                title = stringResource(R.string.join_group),
                subtitle = stringResource(R.string.allow_this_user_to_join_the_group),
                checked = joinGroup,
                onCheckedChange = { joinGroup = it }
            )

            // Remark
            AppTextArea(
                label = stringResource(R.string.remark),
                placeholder = stringResource(R.string.enter_the_remark),
                value = remark,
                onValueChange = { remark = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    val phoneRegex = Regex("^0[0-9]{7,9}$")

                    // === VALIDATION ===
                    latinNameError = if (latinName.isBlank()) context.getString(R.string.latin_name_is_required) else null
                    khmerNameError = if (khmerName.isBlank()) context.getString(R.string.khmer_name_is_required) else null
                    genderError = if (gender == null) context.getString(R.string.please_select_gender) else null

                    emailError = when {
                        email.isBlank() -> context.getString(R.string.email_is_required)
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                            context.getString(R.string.the_email_format_is_incorrect)
                        else -> null
                    }

                    phoneError = when {
                        phone.isBlank() ->
                            context.getString(R.string.please_enter_your_phone_number)
                        !phone.all { it.isDigit() } ->
                            context.getString(R.string.phone_number_must_contain_digits_only)
                        phone.length !in 9..10 ->
                            context.getString(R.string.phone_number_must_be_9_to_10_digits)
                        !phoneRegex.matches(phone) ->
                            context.getString(R.string.invalid_phone_number_format)
                        else -> null
                    }

                    paymentStatusError = if (paymentStatus == null) context.getString(R.string.please_select_payment_status) else null
                    addressError = if (address.isBlank()) context.getString(R.string.address_is_required) else null
                    dobError = if (dob.isBlank()) context.getString(R.string.date_of_birth_is_required) else null
                    registrationDateError = if (registrationDate.isBlank()) context.getString(R.string.registration_date_is_required) else null
                    degreeError = if (degree == null) context.getString(R.string.please_select_degree) else null

                    val hasError =
                        latinNameError != null ||
                                khmerNameError != null ||
                                genderError != null ||
                                emailError != null ||
                                phoneError != null ||
                                paymentStatusError != null ||
                                addressError != null ||
                                dobError != null ||
                                registrationDateError != null ||
                                degreeError != null

                    if (hasError) {
                        focusManager.clearFocus()
                        return@clickable
                    }

                    val member = MemberRegistrationModel(
                        id = 0,
                        latinName = latinName,
                        khmerName = khmerName,
                        gender = gender ?: GenderType.OTHER,
                        email = email,
                        phone = phone,
                        paymentStatus = paymentStatus ?: PaymentStatus.UNPAID,
                        address = address,
                        dob = dob.toApiDate(),
                        registrationDate = registrationDate.toApiDate(),
                        degree = degree ?: DegreeType.UNKNOWN,
                        joinGroup = joinGroup,
                        remark = remark.ifEmpty { "—" }
                    )
                    onSubmit(member)
                    focusManager.clearFocus()
                },
        ) {
            Text(
                stringResource(R.string.submit),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White
                )
            )
        }

        if (isSubmitting) {
            AppLoadingDialog(
                loadingText = stringResource(R.string.submitting)
            )
        }

        submitResult?.let { result ->
            AppCustomDialog(
                title = stringResource(R.string.member_added),
                description = stringResource(R.string.the_member_has_been_successfully_registered),
                result = result,
                onDismiss = {
                    memberRegistrationViewModel.clearSubmitResult()
                },
                onSuccessNavigateBack = {
                    memberRegistrationViewModel.clearSubmitResult()
                    navigator.popBackStack()
                }
            )
        }

    }
}