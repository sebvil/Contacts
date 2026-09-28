package com.sebastianvm.scripts.codegen.models

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

object Constants {

    object Directories {
        const val YAML_MODELS = "models"
    }

    object Modules {
        const val DOMAIN = "domain"
        const val SERVER = "server"
    }

    object Packages {
        const val ROOT = "com.sebastianvm.contacts"
        const val DOMAIN_MODELS = "$ROOT.domain"
        const val DATABASE_TABLES = "$ROOT.database.tables"
        const val EXPOSED_CORE = "org.jetbrains.exposed.v1.core"
    }

    object Types {
        val DATE = ClassName("kotlinx.datetime", "LocalDate")
        val UUID = ClassName("kotlin.uuid", "Uuid")
        val UUID_TABLE = ClassName("${Packages.EXPOSED_CORE}.dao.id", "UuidTable")
        val EXPOSED_COLUMN = ClassName(Packages.EXPOSED_CORE, "Column")
    }

    object Members {
        val EXPOSED_DATE_COLUMN = MemberName("org.jetbrains.exposed.v1.datetime", "date")
    }
}
