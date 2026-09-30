plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.sebastianvm.core"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines)
            implementation(libs.kotlinx.serialization.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }

        named("androidHostTest") {
            dependencies {
                implementation(libs.testBalloon)
                implementation(libs.junit)
            }
        }
    }
}
