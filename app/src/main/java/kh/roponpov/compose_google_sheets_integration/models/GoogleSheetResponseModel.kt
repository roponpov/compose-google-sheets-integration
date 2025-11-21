package kh.roponpov.compose_google_sheets_integration.models

data class GoogleSheetResponseModel(
    val range: String,
    val majorDimension: String? = null,
    val values: List<List<String>>? = null
)
