package kh.roponpov.compose_google_sheets_integration.view.profile

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kh.roponpov.compose_google_sheets_integration.R
import kh.roponpov.compose_google_sheets_integration.core.language.AppLanguage
import kh.roponpov.compose_google_sheets_integration.core.language.getDisplayName
import kh.roponpov.compose_google_sheets_integration.core.prefs.AppPreferences
import kh.roponpov.compose_google_sheets_integration.models.AppThemeMode
import kh.roponpov.compose_google_sheets_integration.viewmodel.LanguageViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.ThemeViewModel
import kh.roponpov.compose_google_sheets_integration.viewmodel.UserViewModel

private enum class ProfileSheetType {
    NONE,
    THEME,
    LANGUAGE,
    LOGOUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigator: NavController,
    userViewModel: UserViewModel,
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel,
) {
    val systemUiController = rememberSystemUiController()
    val primaryColor = MaterialTheme.colorScheme.primary
    primaryColor.luminance() > 0.5f

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    val headerHeight = 300.dp
    val sheetOverlap = 40.dp

    val themeMode by themeViewModel.theme

    // Use enum instead of raw strings
    val languages = listOf(
        AppLanguage.ENGLISH,
        AppLanguage.KHMER,
        AppLanguage.CHINESE,
        AppLanguage.LAO
    )

    val context = LocalContext.current
    val activity = context as? Activity

    val selectedLanguage by languageViewModel.currentLanguage

    val scrollState = rememberScrollState()

    // Bottom sheet state
    var currentSheet by remember { mutableStateOf(ProfileSheetType.NONE) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetVisible by remember { mutableStateOf(false) }

    fun openSheet(type: ProfileSheetType) {
        currentSheet = type
        isSheetVisible = true
    }

    fun closeSheet() {
        isSheetVisible = false
        currentSheet = ProfileSheetType.NONE
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ===== HEADER =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF151B27),
                                Color(0xFF1F3B4F)
                            )
                        )
                    )
            ) {
                AsyncImage(
                    model = userViewModel.user.value?.photoUrl ?: "",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(
                            radiusX = 50.dp,
                            radiusY = 50.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        ),
                    contentScale = ContentScale.Crop
                )

                // Top bar (back + title)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding() .padding(horizontal = 0.dp, vertical = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                    Text(
                        text = stringResource(R.string.profile_settings),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Center profile
                Column(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .fillMaxSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = userViewModel.user.value?.photoUrl ?: "",
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = userViewModel.user.value?.name ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // ===== WHITE SHEET WITH SETTINGS =====
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = headerHeight - sheetOverlap)
                .verticalScroll(scrollState),
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                // ===== Appearance section =====
                TitleSection(stringResource(R.string.appearance))

                // Dark / Light / System -> opens bottom sheet
                FunctionFeatureSection(
                    icon = R.drawable.theme_icon,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.theme),
                    subtitle = when (themeMode) {
                        AppThemeMode.DARK -> stringResource(R.string.dark)
                        AppThemeMode.LIGHT -> stringResource(R.string.light)
                        AppThemeMode.SYSTEM -> stringResource(R.string.system_default)
                    },
                    onClick = { openSheet(ProfileSheetType.THEME) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== Language section =====
                TitleSection(stringResource(R.string.language))

                FunctionFeatureSection(
                    icon = R.drawable.language_icon,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.language),
                    subtitle = selectedLanguage.getDisplayName(),
                    onClick = { openSheet(ProfileSheetType.LANGUAGE) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== Account section =====
                TitleSection(stringResource(R.string.account))

                // Logout row -> opens bottom sheet
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            openSheet(ProfileSheetType.LOGOUT)
                        },
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(18.dp),
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.05f))
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.logout_icon),
                                    contentDescription = "Logout",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(8.dp).align(Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.logout),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.sign_out_from_this_device),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Text(
                            text = stringResource(R.string.logout),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // ===== MODAL BOTTOM SHEETS =====
        if (isSheetVisible && currentSheet != ProfileSheetType.NONE) {
            ModalBottomSheet(
                onDismissRequest = { closeSheet() },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .align(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                                )
                        )
                    }
                }
            ) {
                when (currentSheet) {
                    ProfileSheetType.THEME -> {
                        ThemeSheet(
                            selected = themeMode,
                            onSelect = {
                                themeViewModel.setTheme(it)
                                closeSheet()
                            }
                        )
                    }

                    ProfileSheetType.LANGUAGE -> {
                        LanguageSheet(
                            languages = languages,
                            selected = selectedLanguage,
                            onSelect = { lang ->
                                // 1) If user taps the same language, just close the sheet and do nothing
                                if (lang == selectedLanguage) {
                                    closeSheet()
                                    return@LanguageSheet
                                }

                                // 2) Update ViewModel + save to prefs
                                languageViewModel.setLanguage(lang)

                                // 3) Restart app from root so locale is applied everywhere
                                activity?.let {
                                    AppPreferences.restartAppFromRoot(it, lang) // if your function still has (activity, lang)
                                }

                                // No need closeSheet() here because app is restarting anyway,
                                // but it's harmless if you leave it.
                            }
                        )
                    }


                    ProfileSheetType.LOGOUT -> {
                        LogoutSheet(
                            onCancel = { closeSheet() },
                            onConfirm = {
                                closeSheet()

                                activity?.let {
                                    userViewModel.clear(
                                        context = context,
                                        activity = it,
                                    )
                                }
                            }
                        )
                    }

                    ProfileSheetType.NONE -> Unit
                }
            }
        }
    }
}
