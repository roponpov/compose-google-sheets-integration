package kh.roponpov.compose_google_sheets_integration.repositories

import kh.roponpov.compose_google_sheets_integration.models.DegreeType
import kh.roponpov.compose_google_sheets_integration.models.GenderType
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.models.PaymentStatus

class MemberRegistrationRepository {
    fun fetchMemberRegistration() : List<MemberRegistrationModel> {
        return arrayListOf(
            MemberRegistrationModel(
                id = 1654645,
                latinName = "Jonathan Lee",
                khmerName = "ចនាថាន លី",
                gender = GenderType.MALE,
                email = "jonathan.lee@example.com",
                phone = "0962123456",
                paymentStatus = PaymentStatus.UNPAID,
                address = "Cambodia",
                dob = "1999-04-12",
                registrationDate = "2025-01-05",
                degree = DegreeType.BACHELOR,
                joinGroup = false,
                remark = "Good member"
            ),

            MemberRegistrationModel(
                id = 4566485,
                latinName = "Khmer Son",
                khmerName = "កូន ខ្មែរ",
                gender = GenderType.MALE,
                email = "roponpov@gmail.com",
                phone = "0966679444",
                paymentStatus = PaymentStatus.PAID,
                address = "Cambodia",
                dob = "2002-08-12",
                registrationDate = "2025-01-05",
                degree = DegreeType.HIGH_SCHOOL,
                joinGroup = true,
                remark = "Super great member"
            )
        )
    }
}