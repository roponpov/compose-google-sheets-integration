package kh.roponpov.compose_google_sheets_integration.network

import kh.roponpov.compose_google_sheets_integration.models.AppendResponse
import kh.roponpov.compose_google_sheets_integration.models.GoogleSheetResponseModel
import kh.roponpov.compose_google_sheets_integration.models.UpdateValuesResponseModel
import kh.roponpov.compose_google_sheets_integration.models.ValueRangeModel
import kh.roponpov.compose_google_sheets_integration.repositories.ValueRange
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleSheetsApi {
    @GET("v4/spreadsheets/{sheetId}/values/{range}")
    suspend fun getSheetValues(
        @Path("sheetId") sheetId: String,
        @Path("range") range: String,
        @Query("key") apiKey: String
    ): GoogleSheetResponseModel

    @POST("v4/spreadsheets/{sheetId}/values/{range}:append")
    suspend fun appendValues(
        @Path("sheetId") sheetId: String,
        @Path("range") range: String,
        @Query("valueInputOption") valueInputOption: String = "USER_ENTERED",
        @Body body: ValueRangeModel,
        @Header("Authorization") authHeader: String
    ): AppendResponse

    @PUT("v4/spreadsheets/{spreadsheetId}/values/{range}")
    suspend fun updateValues(
        @Header("Authorization") auth: String,
        @Path("spreadsheetId") spreadsheetId: String,
        @Path("range") range: String,
        @Query("valueInputOption") valueInputOption: String = "USER_ENTERED",
        @Body body: ValueRange
    ): UpdateValuesResponseModel
}

