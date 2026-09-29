plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.routes"
    }

    sourceSets {
        commonMain {
            kotlin.srcDir("src/gen/kotlin")
            dependencies {
                implementation(libs.ktor.resources)
            }
        }
    }
}
