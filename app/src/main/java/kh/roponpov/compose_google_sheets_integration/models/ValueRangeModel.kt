package kh.roponpov.compose_google_sheets_integration.models

import com.squareup.moshi.Json

data class ValueRangeModel(
    @Json(name = "values")
    val values: List<List<String>>
)