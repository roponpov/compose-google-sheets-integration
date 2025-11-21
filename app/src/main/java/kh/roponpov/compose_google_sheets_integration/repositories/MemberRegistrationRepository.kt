package kh.roponpov.compose_google_sheets_integration.repositories

import kh.roponpov.compose_google_sheets_integration.constant.CredentialKeys
import kh.roponpov.compose_google_sheets_integration.models.DegreeType
import kh.roponpov.compose_google_sheets_integration.models.GenderType
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.models.PaymentStatus
import kh.roponpov.compose_google_sheets_integration.models.ValueRangeModel
import kh.roponpov.compose_google_sheets_integration.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRegistrationRepository {

    private val readRange = "B6:N100"
    private val appendRange = "B:N"

    suspend fun fetchMemberRegistration(): List<MemberRegistrationModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.sheetsApi.getSheetValues(
                    sheetId = CredentialKeys.SHEET_ID,
                    range = readRange,
                    apiKey = CredentialKeys.GOOGLE_SHEETS_API_KEY
                )

                val values = response.values ?: emptyList()

                values.mapNotNull { row ->
                    if (row.size < 13) return@mapNotNull null

                    try {
                        MemberRegistrationModel(
                            id = row[0].toIntOrNull() ?: 0,
                            latinName = row[1],
                            khmerName = row[2],
                            gender = genderFromSheet(row[3]),
                            email = row[4],
                            phone = row[5],
                            paymentStatus = paymentStatusFromSheet(row[6]),
                            address = row[7],
                            dob = row[8],
                            registrationDate = row[9],
                            degree = degreeFromSheet(row[10]),
                            joinGroup = joinGroupFromSheet(row[11]),
                            remark = row[12]
                        )
                    } catch (_: Exception) {
                        null
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    suspend fun insertMemberRegistration(
        member: MemberRegistrationModel,
        accessToken: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.sheetsApi.getSheetValues(
                sheetId = CredentialKeys.SHEET_ID,
                range = readRange,
                apiKey = CredentialKeys.GOOGLE_SHEETS_API_KEY
            )
            val values = response.values ?: emptyList()
            val id = getNextId(values)

            val row = listOf(
                id.toString(),
                member.latinName,
                member.khmerName,
                member.gender.text,
                member.email,
                member.phone,
                member.paymentStatus.text,
                member.address,
                member.dob,
                member.registrationDate,
                member.degree.text,
                if (member.joinGroup) "Y" else "N",
                member.remark
            )

            val body = ValueRangeModel(values = listOf(row))

            RetrofitClient.sheetsApi.appendValues(
                sheetId = CredentialKeys.SHEET_ID,
                range = appendRange,
                valueInputOption = "USER_ENTERED",
                body = body,
                authHeader = "Bearer $accessToken"
            )

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getNextId(values: List<List<String>>): Int {
        val lastRow = values.lastOrNull() ?: return 1
        val lastId = lastRow.getOrNull(0)?.toIntOrNull() ?: return 1
        return lastId + 1
    }

    private fun genderFromSheet(text: String): GenderType {
        return when (text.trim().lowercase()) {
            "male" -> GenderType.MALE
            "female" -> GenderType.FEMALE
            else -> GenderType.OTHER
        }
    }

    private fun paymentStatusFromSheet(text: String): PaymentStatus {
        return when (text.trim().lowercase()) {
            "paid" -> PaymentStatus.PAID
            else -> PaymentStatus.UNPAID
        }
    }

    private fun degreeFromSheet(text: String): DegreeType {
        return when (text.trim().lowercase()) {
            "bachelor" -> DegreeType.BACHELOR
            "high school" -> DegreeType.HIGH_SCHOOL
            "master" -> DegreeType.MASTER
            "associate" -> DegreeType.ASSOCIATE
            else -> DegreeType.BACHELOR // fallback
        }
    }

    private fun joinGroupFromSheet(text: String): Boolean {
        // your sheet: "Y" / "N"
        return text.trim().equals("Y", ignoreCase = true)
    }
}
