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
    id("io.github.ben-manes.versions.settings") version "0.61.0"
}

includeBuild(Constants.BUILD_LOGIC_DIR_NAME)

File(rootDir, "app").includeModules()

rootDir.includeModules()

fun File.includeModules() {
    if (!isDirectory) {
        return
    }
    val isModule =
        this != rootDir &&
            name != Constants.BUILD_LOGIC_DIR_NAME &&
            File(this, "build.gradle.kts").exists()
    if (isModule) {
        val relativePath = canonicalPath.substringAfter(rootDir.canonicalPath)
        val moduleName = relativePath.replace(File.separator, ":")
        include(moduleName)
    } else {
        listFiles { it.isDirectory }!!.forEach { it.includeModules() }
    }
}

object Constants {
    const val BUILD_LOGIC_DIR_NAME = "build-logic"
}
