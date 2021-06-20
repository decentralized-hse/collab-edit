enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

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

include("chronofold")
include("client")
include("protocol-signal")
include("server-signal")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val kotlinxSerialization = version("kotlinxSerialization", "1.0.1")
            alias("kotlinx-serialization-core").to("org.jetbrains.kotlinx", "kotlinx-serialization-core")
                .versionRef(kotlinxSerialization)
            alias("kotlinx-serialization-json").to("org.jetbrains.kotlinx", "kotlinx-serialization-json")
                .versionRef(kotlinxSerialization)

            val kotlinVersion: String by settings
            val kotlin = version("kotlin", kotlinVersion)
            alias("kotlin-test-common").to("org.jetbrains.kotlin", "kotlin-test-common").versionRef(kotlin)
            alias("kotlin-test-annotations-common").to("org.jetbrains.kotlin", "kotlin-test-annotations-common")
                .versionRef(kotlin)
            alias("kotlin-test-js").to("org.jetbrains.kotlin", "kotlin-test-js").versionRef(kotlin)

            val kotest = version("kotest", "4.4.0")
            alias("kotest-assertions-core").to("io.kotest", "kotest-assertions-core").versionRef(kotest)
            alias("kotest-runner-junit5").to("io.kotest", "kotest-runner-junit5").versionRef(kotest)

            val selenide = version("selenide", "5.13.1")
            alias("selenide").to("com.codeborne", "selenide").versionRef(selenide)

            val kotlinxCoroutines = version("kotlinxCoroutines", "1.4.1")
            alias("kotlinx-coroutines-core").to("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
                .versionRef(kotlinxCoroutines)

            val ktor = version("ktor", "1.5.1")
            alias("ktor-server-netty").to("io.ktor", "ktor-server-netty").versionRef(ktor)
            alias("ktor-server-core").to("io.ktor", "ktor-server-core").versionRef(ktor)
            alias("ktor-websockets").to("io.ktor", "ktor-websockets").versionRef(ktor)

            val logback = version("logback", "1.2.1")
            alias("logback-classic").to("ch.qos.logback", "logback-classic").versionRef(logback)

            val kotlinxHtml = version("kotlinxHtml", "0.7.2")
            alias("kotlinx-html").to("org.jetbrains.kotlinx", "kotlinx-html").versionRef(kotlinxHtml)

            val kotlinExtensions = version("kotlinExtensions", "1.0.1-pre.144-kotlin-1.4.21")
            alias("kotlin-extensions").to("org.jetbrains", "kotlin-extensions").versionRef(kotlinExtensions)

            val kotlinReact = version("kotlinReact", "17.0.1-pre.144-kotlin-1.4.21")
            alias("kotlin-react-react").to("org.jetbrains", "kotlin-react").versionRef(kotlinReact)
            alias("kotlin-react-dom").to("org.jetbrains", "kotlin-react-dom").versionRef(kotlinReact)

            val kotlinStyled = version("kotlinStyled", "5.2.1-pre.148-kotlin-1.4.21")
            alias("kotlin-styled").to("org.jetbrains", "kotlin-styled").versionRef(kotlinStyled)
        }
    }
}
