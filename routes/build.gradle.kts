plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.routes"
    }

    sourceSets {

        commonMain.dependencies {
            implementation(libs.ktor.resources)
        }
    }
}