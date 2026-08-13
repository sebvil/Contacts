plugins {
    alias(libs.plugins.kmpLibrary)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.core"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines)
        }
    }
}
