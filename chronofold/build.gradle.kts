plugins {
    kotlin("multiplatform")
}

val kotestVersion: String by project
val kotlinVersion: String by project

kotlin {
    js(IR) {
        browser()
    }

    sourceSets {
        getByName("commonTest") {
            dependencies {
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation(kotlin("test-common", kotlinVersion))
                implementation(kotlin("test-annotations-common", kotlinVersion))
            }
        }

        getByName("jsTest") {
            dependencies {
                implementation(kotlin("test-js", kotlinVersion))
            }
        }
    }
}
