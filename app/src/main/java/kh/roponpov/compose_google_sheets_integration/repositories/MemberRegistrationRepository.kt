package kh.roponpov.compose_google_sheets_integration.repositories

import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel

class MemberRegistrationRepository {
    fun fetchMemberRegistration() : List<MemberRegistrationModel> {
        return arrayListOf(
            MemberRegistrationModel(
                id = 1,
                latinName = "Jonathan Lee",
                khmerName = "ចនាថាន លី",
                gender = "Male",
                email = "jonathan.lee@example.com",
                phone = "0962123456",
                paymentStatus = "Paid",
                address = "Phnom Penh",
                dob = "1999-04-12",
                registrationDate = "2025-01-05",
                degree = "Bachelor",
                joinGroup = false,
                remark = "Good member"
            )
        )
    }
}