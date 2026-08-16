plugins {
    alias(libs.plugins.kmpLibrary)
}

kotlin {
    android {
        namespace = "com.sebastianvm.core"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines)
        }
    }
}
