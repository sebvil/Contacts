plugins {
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    compilerOptions {
        // TODO remove when
        // https://github.com/sqldelight/sqldelight/commit/0cce908ac43c31e480b1eeea2dbfdb6be17e9b3a
        // is released
        freeCompilerArgs.addAll(
            "-Xwarning-level=REDUNDANT_VISIBILITY_MODIFIER:warning",
            "-Xwarning-level=ASSIGNED_VALUE_IS_NEVER_READ:warning",
        )
    }
    explicitApi()
    android {
        namespace = "com.sebastianvm.contacts.app.database"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.jvm.driver)
        }

        jsMain.dependencies {
            implementation(libs.sqldelight.webworker.driver)
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
            implementation(npm("sql.js", "1.8.0"))
            implementation(devNpm("copy-webpack-plugin", "12.0.2"))
        }

        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(libs.sqldelight.coroutines)
            api(project(":core"))
        }
    }
}

sqldelight {
    databases {
        register("Database") {
            packageName.set("com.sebastianvm.contacts.app.database")
            generateAsync.set(true)
        }
    }
}
