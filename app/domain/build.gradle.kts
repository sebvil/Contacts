plugins {
    alias(libs.plugins.kmpLibrary)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.domain"
    }

    sourceSets {
        commonMain {
            kotlin.srcDir("src/gen/kotlin")
        }
    }
}
