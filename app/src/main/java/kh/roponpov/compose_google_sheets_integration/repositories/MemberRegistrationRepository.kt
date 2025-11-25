package kh.roponpov.compose_google_sheets_integration.repositories

import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.constant.CredentialKeys
import kh.roponpov.compose_google_sheets_integration.models.BatchUpdateRequestModel
import kh.roponpov.compose_google_sheets_integration.models.BatchUpdateResponseModel
import kh.roponpov.compose_google_sheets_integration.models.ValueRangeModel
import kh.roponpov.compose_google_sheets_integration.core.network.NetworkService
import kh.roponpov.compose_google_sheets_integration.models.PaymentStatus
import kh.roponpov.compose_google_sheets_integration.models.DegreeType
import kh.roponpov.compose_google_sheets_integration.models.DeleteDimensionRequestModel
import kh.roponpov.compose_google_sheets_integration.models.DimensionRangeModel
import kh.roponpov.compose_google_sheets_integration.models.DynamicValueRangeModel
import kh.roponpov.compose_google_sheets_integration.models.GenderType
import kh.roponpov.compose_google_sheets_integration.models.RequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRegistrationRepository {

    private val readRange = "B6:N100"
    private val appendRange = "B:N"
    private val sheetTabId = 0

    suspend fun fetchMemberRegistration(): List<MemberRegistrationModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = NetworkService.sheetsApi.getSheetValues(
                    sheetId = CredentialKeys.SHEET_ID,
                    range = readRange,
                    apiKey = CredentialKeys.GOOGLE_SHEETS_API_KEY
                )

                val values = response.values ?: emptyList()
                val startRow = 6

                values.mapIndexedNotNull { index, row ->
                    val rowIndex = startRow + index
                    try {
                        MemberRegistrationModel(
                            indexRange = rowIndex,
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
            val response = NetworkService.sheetsApi.getSheetValues(
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

            NetworkService.sheetsApi.appendValues(
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

    suspend fun updateMemberRegistration(
        member: MemberRegistrationModel,
        accessToken: String
    ): Boolean {
        val row = member.indexRange ?: return false

        val range = "B$row:N$row"

        val values = listOf(
            listOf(
                member.id,
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
                member.remark,
            )
        )

        val body = DynamicValueRangeModel(values = values)

        val response = NetworkService.sheetsApi.updateValues(
            auth = "Bearer $accessToken",
            spreadsheetId = CredentialKeys.SHEET_ID,
            range = range,
            valueInputOption = "USER_ENTERED",
            body = body
        )

        return (response.updatedRows ?: 0) > 0
    }

    suspend fun deleteMemberRegistration(
        member: MemberRegistrationModel,
        accessToken: String
    ): Boolean = withContext(Dispatchers.IO) {
        val rowIndex = member.indexRange ?: return@withContext false

        // Google Sheet rows are 0-based in DeleteDimension:
        // row 1 -> index 0, row 2 -> 1, ...
        val startIndex = rowIndex - 1
        val endIndex = rowIndex

        val requestBody = BatchUpdateRequestModel(
            requests = listOf(
                RequestModel(
                    deleteDimension = DeleteDimensionRequestModel(
                        range = DimensionRangeModel(
                            sheetId = sheetTabId,
                            dimension = "ROWS",
                            startIndex = startIndex,
                            endIndex = endIndex
                        )
                    )
                )
            )
        )

        try {
            val response: BatchUpdateResponseModel =
                NetworkService.sheetsApi.batchUpdate(
                    auth = "Bearer $accessToken",
                    spreadsheetId = CredentialKeys.SHEET_ID,
                    body = requestBody
                )

            // if we got a spreadsheetId back, we assume it went fine
            response.spreadsheetId != null
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
        return text.trim().equals("Y", ignoreCase = true)
    }
}
