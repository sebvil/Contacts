import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(libs.versions.java.get())
    }
}

dependencies {
    implementation(libs.bundles.buildLogic)
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "androidApp"
            implementationClass = "com.sebastianvm.contacts.buildlogic.AndroidApplicationPlugin"
        }

        register("desktopApp") {
            id = "desktopApp"
            implementationClass = "com.sebastianvm.contacts.buildlogic.DesktopApplicationPlugin"
        }

        register("kmpComposeLibrary") {
            id = "kmpComposeLibrary"
            implementationClass = "com.sebastianvm.contacts.buildlogic.KmpComposeLibraryPlugin"
        }

        register("webApp") {
            id = "webApp"
            implementationClass = "com.sebastianvm.contacts.buildlogic.WebApplicationPlugin"
        }

        register("jvmApp") {
            id = "jvmApp"
            implementationClass = "com.sebastianvm.contacts.buildlogic.JvmApplicationPlugin"
        }

        register("kmpLibrary") {
            id = "kmpLibrary"
            implementationClass = "com.sebastianvm.contacts.buildlogic.KmpLibraryPlugin"
        }
    }
}
