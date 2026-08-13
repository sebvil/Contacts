rootProject.name = "contacts"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("io.github.ben-manes.versions.settings") version "0.56.0"
}

includeBuild("build-logic")

include(":app:androidApp")

include(":app:desktopApp")

include(":app:shared")

include(":app:webApp")

include(":server")

include(":routes")

include(":domain")

include(":fixtures")

include(":app:database")

include(":core")
