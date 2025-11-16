package kh.roponpov.compose_google_sheets_integration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.repositories.MemberRegistrationRepository
import kotlinx.coroutines.launch

class MemberRegistrationViewModel () : ViewModel() {
    private val memberRegistrationRepository: MemberRegistrationRepository = MemberRegistrationRepository()
    private val _memberRegistrations = MutableLiveData<List<MemberRegistrationModel>>()
    val memberRegistrations: LiveData<List<MemberRegistrationModel>> = _memberRegistrations

    fun getMemberRegistration() {
        viewModelScope.launch {
            val memberRegistrationResult = memberRegistrationRepository.fetchMemberRegistration()
            _memberRegistrations.postValue(memberRegistrationResult)
        }
    }
}