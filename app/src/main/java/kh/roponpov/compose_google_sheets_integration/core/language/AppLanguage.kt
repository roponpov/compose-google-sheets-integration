package kh.roponpov.compose_google_sheets_integration.core.language

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kh.roponpov.compose_google_sheets_integration.R
import java.util.Locale

enum class AppLanguage(
    val code: String,
    val locale: Locale,
    val nameRes: Int,
    val flagRes: Int
) {
    KHMER(
        code = "km",
        locale = Locale("km"),
        nameRes = R.string.language_display_khmer,
        flagRes = R.drawable.cambodia_flag
    ),

    ENGLISH(
        code = "en",
        locale = Locale.ENGLISH,
        nameRes = R.string.language_display_english,
        flagRes = R.drawable.english_flag
    ),

    CHINESE(
        code = "zh",
        locale = Locale.SIMPLIFIED_CHINESE,
        nameRes = R.string.language_display_chinese,
        flagRes = R.drawable.china_flag
    ),

    LAO(
        code = "lo",
        locale = Locale("lo"),
        nameRes = R.string.language_display_lao,
        flagRes = R.drawable.lao_flag
    );

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.find { it.code == code } ?: KHMER
        }
    }
}

@Composable
fun AppLanguage.getDisplayName(): String {
    return stringResource(id = nameRes)
}