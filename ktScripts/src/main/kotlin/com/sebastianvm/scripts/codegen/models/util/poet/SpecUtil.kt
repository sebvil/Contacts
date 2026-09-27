package com.sebastianvm.scripts.codegen.models.util.poet

import com.squareup.kotlinpoet.FileSpec
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

fun fileSpecBuilder(packageName: String, fileName: String): FileSpec.Builder {
    return FileSpec.builder(packageName, fileName).addKotlinDefaultImports().indent("    ")
}

fun FileSpec.writeTo(module: String) {
    val directoryName =
        "app/${module.replace(':', '/')}/src/gen/kotlin/" + packageName.replace('.', '/')
    val fileContents = buildString {
        this@writeTo.writeTo(this)
    }
    val fileWithoutPublicModifiers = fileContents.replace("public ", "")
    Path(directoryName).createDirectories()
    File(directoryName, "$name.kt").writeText(fileWithoutPublicModifiers)
}
