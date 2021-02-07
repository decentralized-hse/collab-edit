plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val kxSerializationVersion: String by project

kotlin {
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kxSerializationVersion")
            }
        }
    }
}
