package com.sebastianvm.scripts.codegen.models.generators

import com.squareup.kotlinpoet.FileSpec

interface Generator {

    fun generate(): FileSpec
}
