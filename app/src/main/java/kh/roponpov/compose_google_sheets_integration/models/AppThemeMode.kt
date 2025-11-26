package kh.roponpov.compose_google_sheets_integration.models

enum class AppThemeMode(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromValue(value: String?): AppThemeMode {
            return entries.find { it.value == value } ?: SYSTEM
        }
    }
}
