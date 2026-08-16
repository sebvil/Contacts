plugins {
    alias(libs.plugins.webApp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":app:client:shared"))
            implementation(libs.compose.ui)
        }
    }
}
