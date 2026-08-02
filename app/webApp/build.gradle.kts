plugins {
    alias(libs.plugins.webApp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.shared)

            implementation(libs.compose.ui)
        }
    }
}
