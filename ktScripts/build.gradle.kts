plugins {
    alias(libs.plugins.jvmApp)
}

group = "com.sebastianvm.scripts"

version = "1.0.0"

application {
    mainClass = "com.sebastianvm.scripts.MainKt"
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}


dependencies {
    implementation(libs.clikt)
}
