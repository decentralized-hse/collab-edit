plugins {
    kotlin("js")
}

val kotestVersion: String by project
val kotlinVersion: String by project

dependencies {
    implementation("io.kotest:kotest-assertions-core:$kotestVersion")
    implementation(kotlin("test-js", kotlinVersion))
}

kotlin {
    js(IR) {
        browser()
    }
}
