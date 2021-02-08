import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackCssMode.EXTRACT

plugins {
    kotlin("js")
}

repositories {
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

val kotlinxHtmlVersion: String by project
val webrtcAdapterVersion: String by project
val kotlinExtensionsVersion: String by project
val bootstrapVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")
    implementation("org.jetbrains:kotlin-extensions:$kotlinExtensionsVersion")
    implementation(npm("webrtc-adapter", webrtcAdapterVersion))
    implementation(npm("bootstrap", bootstrapVersion))
    implementation(project(":protocol-signal"))
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()

            webpackTask {
                cssSupport.enabled = true
                cssSupport.mode = EXTRACT
            }
        }
    }
}