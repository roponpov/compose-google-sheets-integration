package kh.roponpov.compose_google_sheets_integration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun addMember(member: MemberRegistrationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val ok = memberRegistrationRepository.addMember(member)
            if (ok) {
                // reload from sheet or optimistically append to LiveData
                val current = _memberRegistrations.value.orEmpty()
                val updated = current + member
                withContext(Dispatchers.Main) {
                    _memberRegistrations.value = updated
                }
            } else {
                // handle error (toast/snackbar)
            }
        }
    }
}