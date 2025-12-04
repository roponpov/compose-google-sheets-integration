import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "kh.roponpov.compose_google_sheets_integration"
    compileSdk = 35

    val keystorePropsFile = File(rootDir, "keystore.properties")
    val keystoreProps = Properties().apply {
        if (keystorePropsFile.exists()) {
            FileInputStream(keystorePropsFile).use { load(it) }
        }
    }

    signingConfigs {
        // 🔐 2) CREATE RELEASE SIGNING CONFIG
        create("release") {
            // Option A: load from keystore.properties
            storeFile = keystoreProps["storeFile"]?.let { file(it as String) }
            storePassword = keystoreProps["storePassword"] as String?
            keyAlias = keystoreProps["keyAlias"] as String?
            keyPassword = keystoreProps["keyPassword"] as String?

            // 👉 OR for quick test, hardcode (NOT for production)
            // storeFile = file("my-release-key.jks")
            // storePassword = "yourStorePassword"
            // keyAlias = "yourAlias"
            // keyPassword = "yourKeyPassword"
        }
    }

    defaultConfig {

        val localProps = Properties().apply {
            val file = File(rootDir, "local.properties")
            if (file.exists()) {
                file.inputStream().use { load(it) }
            }
        }

        val clientId = localProps.getProperty("CLIENT_ID") ?: ""
        val sheetId = localProps.getProperty("SHEET_ID") ?: ""
        val googleSheetsApiKey = localProps.getProperty("GOOGLE_SHEETS_API_KEY") ?: ""

        applicationId = "kh.roponpov.compose_google_sheets_integration"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"$clientId\""
        )
        buildConfigField(
            "String",
            "SHEET_ID",
            "\"$sheetId\""
        )
        buildConfigField(
            "String",
            "GOOGLE_SHEETS_API_KEY",
            "\"$googleSheetsApiKey\""
        )
    }

    buildTypes {
        release {
            // 🔑 3) TELL RELEASE TO USE THE SIGNING CONFIG
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
    implementation(libs.play.services.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.moshi.kotlin.codegen)
    implementation(libs.logging.interceptor)
    implementation (libs.accompanist.systemuicontroller)
    implementation(libs.coil.compose)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}