plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        browser()
    }

    sourceSets {
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
