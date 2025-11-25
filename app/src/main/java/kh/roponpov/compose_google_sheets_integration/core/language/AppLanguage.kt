package kh.roponpov.compose_google_sheets_integration.core.language

import kh.roponpov.compose_google_sheets_integration.R
import java.util.Locale

enum class AppLanguage(
    val code: String,
    val locale: Locale,
    val displayName: String,
    val flagRes: Int
) {
    KHMER(
        code = "km",
        locale = Locale("km"),
        displayName = "Khmer",
        flagRes = R.drawable.cambodia_flag
    ),

    ENGLISH(
        code = "en",
        locale = Locale.ENGLISH,
        displayName = "English",
        flagRes = R.drawable.english_flag
    ),

    CHINESE(
        code = "zh",
        locale = Locale.SIMPLIFIED_CHINESE,
        displayName = "Chinese",
        flagRes = R.drawable.china_flag
    ),

    LAO(
        code = "lo",
        locale = Locale("lo"),
        displayName = "Lao",
        flagRes = R.drawable.lao_flag
    );

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.find { it.code == code } ?: KHMER
        }
    }
}