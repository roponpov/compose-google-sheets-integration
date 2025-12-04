package kh.roponpov.compose_google_sheets_integration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.roponpov.compose_google_sheets_integration.R
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.repositories.MemberRegistrationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    sealed class SubmitResult {
        data object Success : SubmitResult()
        data class Error(val message: Int) : SubmitResult()
    }

    private val _submitResult = MutableLiveData<SubmitResult?>(null)
    val submitResult: LiveData<SubmitResult?> = _submitResult

    private val _deleteResult = MutableLiveData<SubmitResult?>(null)
    val deleteResult: LiveData<SubmitResult?> = _deleteResult

    fun clearSubmitResult() {
        _submitResult.value = null
    }

    fun clearDeleteResult() {
        _deleteResult.value = null
    }

    fun refreshMembers() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(200)
            _memberRegistrations.value = memberRegistrationRepository.fetchMemberRegistration().reversed()
            _isRefreshing.value = false
        }
    }

    fun getMemberRegistration() {
        viewModelScope.launch(Dispatchers.IO) {

            _isLoading.postValue(true)

            val data = try {
                memberRegistrationRepository.fetchMemberRegistration()
            } catch (_: Exception) {
                emptyList()
            }

            _memberRegistrations.postValue(data)
            _isLoading.postValue(false)
        }
    }

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
                        SubmitResult.Error(R.string.failed_to_submit_registration)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _submitResult.value =
                        SubmitResult.Error(R.string.unexpected_error_occurred)
                }
            } finally {
                withContext(Dispatchers.Main) { _isSubmitting.value = false }
            }
        }
    }

    fun updateMember(member: MemberRegistrationModel, accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { _isSubmitting.value = true }

                val success = memberRegistrationRepository.updateMemberRegistration(
                    member = member,
                    accessToken = accessToken,
                )

                withContext(Dispatchers.Main) {
                    _submitResult.value = if (success) {
                        SubmitResult.Success
                    } else {
                        SubmitResult.Error(R.string.failed_to_update_member)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _submitResult.value =
                        SubmitResult.Error(R.string.unexpected_error_occurred)
                }
            } finally {
                withContext(Dispatchers.Main) { _isSubmitting.value = false }
            }
        }
    }

    fun deleteMember(member: MemberRegistrationModel, accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { _isSubmitting.value = true }

                val success = memberRegistrationRepository.deleteMemberRegistration(
                    member = member,
                    accessToken = accessToken
                )

                withContext(Dispatchers.Main) {
                    _deleteResult.value = if (success) {
                        SubmitResult.Success
                    } else {
                        SubmitResult.Error(R.string.failed_to_delete_member)
                    }
                }

                if (success) {
                    val data = memberRegistrationRepository.fetchMemberRegistration()
                    _memberRegistrations.postValue(data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _deleteResult.value =
                        SubmitResult.Error(R.string.unexpected_error_occurred)
                }
            } finally {
                withContext(Dispatchers.Main) { _isSubmitting.value = false }
            }
        }
    }

}