plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotlin.test.common)
                implementation(libs.kotlin.test.annotations.common)
            }
        }

        getByName("jsTest") {
            dependencies {
                implementation(libs.kotlin.test.js)
            }
        }
    }
}
