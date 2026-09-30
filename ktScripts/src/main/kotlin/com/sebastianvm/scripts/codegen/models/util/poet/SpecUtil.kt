package com.sebastianvm.scripts.codegen.models.util.poet

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlinx.serialization.Serializable

fun fileSpecBuilder(packageName: String, fileName: String): FileSpec.Builder {
    return FileSpec.builder(packageName, fileName).addKotlinDefaultImports().indent("    ")
}

fun FileSpec.writeTo(module: String) {
    val directoryName =
        "app/${module.replace(':', '/')}/src/gen/kotlin/" + packageName.replace('.', '/')
    val fileContents = getProcessedFileContents()
    Path(directoryName).createDirectories()
    File(directoryName, "$name.kt").writeText(fileContents)
}

fun FileSpec.getProcessedFileContents(): String {
    val fileContents = buildString {
        this@getProcessedFileContents.writeTo(this)
    }
    return fileContents.replace("public ", "")
}

fun TypeSpec.Builder.addSerializableAnnotation(): TypeSpec.Builder {
    return addAnnotation(Serializable::class)
}
