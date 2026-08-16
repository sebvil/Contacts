plugins {
    alias(libs.plugins.kmpLibrary)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.fixtures"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":app:domain"))
            implementation(project(":app:routes"))
        }
    }
}
