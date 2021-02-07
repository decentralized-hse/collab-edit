plugins {
    kotlin("js") apply false
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
}

allprojects {
    group = "com.github.servb"
    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/ktor")
    }
}
