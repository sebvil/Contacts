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
                implementation(project(":core"))
                implementation(libs.ktor.resources)
                implementation(libs.datetime)
            }
        }
    }
}
