import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.example.adaxintegra"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.adaxintegra"
        minSdk = 29

        targetSdk = 37
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "env"

    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            buildConfigField(
                "String",
                "SUPABASE_URL",
                "\"${localProperties.getProperty("SUPABASE_URL_DEV", "")}\"",
            )
            buildConfigField(
                "String",
                "SUPABASE_ANON_KEY",
                "\"${localProperties.getProperty("SUPABASE_ANON_KEY_DEV", "")}\"",
            )
        }
        create("prod") {
            dimension = "env"
            buildConfigField(
                "String",
                "SUPABASE_URL",
                "\"${localProperties.getProperty("SUPABASE_URL_PROD", "")}\"",
            )
            buildConfigField(
                "String",
                "SUPABASE_ANON_KEY",
                "\"${localProperties.getProperty("SUPABASE_ANON_KEY_PROD", "")}\"",
            )
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.compose.material:material-icons-extended")

    // Supabase
    implementation(platform(libs.supabase.bom))
    implementation(libs.protolite.well.known.types)
    implementation(libs.supabase.auth)
    implementation(libs.ktor.client.android)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.ui.tooling)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.okhttp.logging.interceptor)

    // Coil: loads the event images from their URL (E-01)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Security
    implementation(libs.androidx.security.crypto)
}

// Automatically install the git hook before building
tasks.named("preBuild") {
    dependsOn(rootProject.tasks.named("addKtlintFormatGitPreCommitHook"))
}
