plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
