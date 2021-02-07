pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("js") version kotlinVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
    }
}

rootProject.name = "collab-edit"

include("client")
include("protocol-signal")
include("server-signal")
