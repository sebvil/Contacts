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
        const val ROUTES = "routes"
    }

    object Packages {
        const val ROOT = "com.sebastianvm.contacts"
        const val DTO = "$ROOT.dto"
        const val TYPES = "com.sebastianvm.core.types"
        const val DOMAIN_MODELS = "$ROOT.domain"
        const val ROUTES = "$ROOT.routes"
        const val DATABASE_TABLES = "$ROOT.database.tables"
        const val EXPOSED_CORE = "org.jetbrains.exposed.v1.core"
        const val METRO = "dev.zacsweers.metro"
    }

    object Types {
        val DATE = ClassName("kotlinx.datetime", "LocalDate")
        val INSTANT = ClassName("kotlin.time", "Instant")
        val UUID = ClassName("kotlin.uuid", "Uuid")
        val UUID_TABLE = ClassName("${Packages.EXPOSED_CORE}.dao.id", "UuidTable")
        val ID_TABLE = ClassName("${Packages.EXPOSED_CORE}.dao.id", "IdTable")
        val EXPOSED_COLUMN = ClassName(Packages.EXPOSED_CORE, "Column")
        val CONTRIBUTES_INTO_SET = ClassName(Packages.METRO, "ContributesIntoSet")
        val APP_SCOPE = ClassName(Packages.METRO, "AppScope")
        val BINDING = ClassName(Packages.METRO, "binding")
        val EXPOSED_REFERENCE_OPTION = ClassName(Packages.EXPOSED_CORE, "ReferenceOption")

        val KTOR_RESOURCE = ClassName("io.ktor.resources", "Resource")
        val OPTION = ClassName(Packages.TYPES, "Option")
        val NONE = ClassName(Packages.TYPES, "None")
    }

    object Members {
        val EXPOSED_DATE_COLUMN = MemberName("org.jetbrains.exposed.v1.datetime", "date")
        val EXPOSED_TIMESTAMP_COLUMN = MemberName("org.jetbrains.exposed.v1.datetime", "timestamp")
        val EXPOSED_CURRENT_TIMESTAMP =
            MemberName("org.jetbrains.exposed.v1.datetime", "CurrentTimestamp")
    }
}
