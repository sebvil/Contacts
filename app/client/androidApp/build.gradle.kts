plugins {
    alias(libs.plugins.androidApp)
}

dependencies {
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "com.sebastianvm.contacts"

    defaultConfig {
        applicationId = "com.sebastianvm.contacts"
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
