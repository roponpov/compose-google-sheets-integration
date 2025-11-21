package kh.roponpov.compose_google_sheets_integration.network

import kh.roponpov.compose_google_sheets_integration.models.GoogleSheetResponseModel
import kh.roponpov.compose_google_sheets_integration.models.SheetAppendRequestModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    suspend fun appendRow(
        @Path("sheetId") sheetId: String,
        @Path("range") range: String,
        @Query("valueInputOption") valueInputOption: String = "USER_ENTERED",
        @Query("insertDataOption") insertDataOption: String = "INSERT_ROWS",
        @Query("key") apiKey: String,
        @Body body: SheetAppendRequestModel
    ): retrofit2.Response<Unit>
}

