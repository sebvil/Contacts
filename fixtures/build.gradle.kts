plugins {
    alias(libs.plugins.kmpLibrary)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.fixtures"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":routes"))
        }
    }
}
