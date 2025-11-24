package kh.roponpov.compose_google_sheets_integration.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BatchUpdateRequestModel(
    @Json(name = "requests")
    val requests: List<RequestModel>
)

@JsonClass(generateAdapter = true)
data class RequestModel(
    @Json(name = "deleteDimension")
    val deleteDimension: DeleteDimensionRequestModel? = null
)

@JsonClass(generateAdapter = true)
data class DeleteDimensionRequestModel(
    @Json(name = "range")
    val range: DimensionRangeModel
)

@JsonClass(generateAdapter = true)
data class DimensionRangeModel(
    @Json(name = "sheetId")
    val sheetId: Int,

    @Json(name = "dimension")
    val dimension: String, // "ROWS"

    @Json(name = "startIndex")
    val startIndex: Int,

    @Json(name = "endIndex")
    val endIndex: Int
)

@JsonClass(generateAdapter = true)
data class BatchUpdateResponseModel(
    @Json(name = "spreadsheetId")
    val spreadsheetId: String? = null
)