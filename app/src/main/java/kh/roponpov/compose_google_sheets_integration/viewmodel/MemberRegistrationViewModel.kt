package kh.roponpov.compose_google_sheets_integration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.roponpov.compose_google_sheets_integration.models.GoogleAuthManager
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.repositories.MemberRegistrationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberRegistrationViewModel : ViewModel() {
    private val memberRegistrationRepository: MemberRegistrationRepository = MemberRegistrationRepository()

    private val _memberRegistrations = MutableLiveData<List<MemberRegistrationModel>>()
    val memberRegistrations: LiveData<List<MemberRegistrationModel>> = _memberRegistrations

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSubmitting = MutableLiveData(false)
    val isSubmitting: LiveData<Boolean> = _isSubmitting

    sealed class SubmitResult {
        data object Success : SubmitResult()
        data class Error(val message: String) : SubmitResult()
    }

    private val _submitResult = MutableLiveData<SubmitResult?>(null)
    val submitResult: LiveData<SubmitResult?> = _submitResult

    fun clearSubmitResult() {
        _submitResult.value = null
    }

    fun getMemberRegistration() {
        viewModelScope.launch(Dispatchers.IO) {

            _isLoading.postValue(true)

            val data = try {
                memberRegistrationRepository.fetchMemberRegistration()
            } catch (e: Exception) {
                emptyList()
            }

            _memberRegistrations.postValue(data)
            _isLoading.postValue(false)
        }
    }

    // 🔥 Call this from AddMemberScreen when user taps Submit
    fun submitMember(member: MemberRegistrationModel, accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { _isSubmitting.value = true }

                val success = memberRegistrationRepository.insertMemberRegistration(
                    member = member,
                    accessToken = accessToken
                )

                withContext(Dispatchers.Main) {
                    _submitResult.value = if (success) {
                        SubmitResult.Success
                    } else {
                        SubmitResult.Error("Failed to submit registration. Please try again.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _submitResult.value =
                        SubmitResult.Error(e.message ?: "Unexpected error occurred.")
                }
            } finally {
                withContext(Dispatchers.Main) { _isSubmitting.value = false }
            }
        }
    }
}