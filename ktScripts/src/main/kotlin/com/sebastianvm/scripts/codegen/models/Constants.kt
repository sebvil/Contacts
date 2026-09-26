package com.sebastianvm.scripts.codegen.models

import com.squareup.kotlinpoet.ClassName

object Constants {

    object Directories {
        const val YAML_MODELS = "models"
    }

    object Modules {
        const val DOMAIN = "domain"
    }

    object Packages {
        const val ROOT = "com.sebastianvm.contacts"
        const val DOMAIN_MODELS = "$ROOT.domain"
    }

    object Types {
        val UUID = ClassName("kotlin.uuid", "Uuid")
    }
}
