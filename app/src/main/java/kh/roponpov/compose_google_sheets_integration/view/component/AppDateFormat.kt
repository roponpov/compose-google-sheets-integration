package kh.roponpov.compose_google_sheets_integration.view.component

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toApiDate(): String {
    return try {
        val input = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = input.parse(this)
        output.format(date!!)
    } catch (e: Exception) {
        this
    }
}

fun Long.toDateString(
    pattern: String = "dd/MM/yyyy",
    locale: Locale = Locale.getDefault()
): String {
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(Date(this))
}