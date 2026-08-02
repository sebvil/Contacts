import kotlin.collections.forEach

plugins {
    alias(libs.plugins.jvmApp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "com.sebastianvm.contacts"

version = "1.0.0"

application {
    mainClass = "com.sebastianvm.contacts.ApplicationKt"
}

dependencies {
    implementation(project(":routes"))
    implementation(project(":domain"))
    implementation(libs.logback)
    implementation(libs.bundles.ktorServer)
    implementation(libs.bundles.exposed)
    testImplementation(libs.bundles.ktorClient)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.testcontainers.postgresql)
}

tasks.register<Exec>("startDb") {
    description = "Starts the database"
    commandLine("docker", "compose", "up", "-d")
}

tasks.register("waitForDb") {
    group = "docker"
    description = "Waits until Postgres is ready to accept connections"
    dependsOn("startDb")

    val projectDir = project.projectDir

    doLast {
        val maxAttempts = 30
        val delayMs = 1000L
        var attempt = 0
        var ready = false
        val dbUser = System.getenv("DB_USER") ?: "devuser"

        while (attempt < maxAttempts && !ready) {
            val process =
                ProcessBuilder(
                        "docker",
                        "compose",
                        "exec",
                        "-T",
                        "postgres",
                        "pg_isready",
                        "-U",
                        dbUser,
                    )
                    .directory(projectDir)
                    .redirectErrorStream(true)
                    .start()

            val exitCode = process.waitFor()

            ready = exitCode == 0

            if (!ready) {
                attempt++
                logger.lifecycle("Waiting for Postgres... (attempt $attempt/$maxAttempts)")
                Thread.sleep(delayMs)
            }
        }

        if (!ready) {
            throw GradleException("Postgres did not become ready after $maxAttempts attempts")
        }
        println("Postgres is ready.")
    }
}

tasks.named<JavaExec>("run") {
    file(".env").readLines().forEach {
        if (it.isNotEmpty() && !it.startsWith("#")) {
            val equalsPos = it.indexOf("=")
            val key = it.substring(0, equalsPos)
            val value = it.substring(equalsPos + 1)

            if (System.getenv(key) == null) {
                environment[key] = value
            }
        }
    }
    dependsOn("waitForDb")
}
