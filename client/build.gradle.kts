import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackCssMode.EXTRACT

plugins {
    kotlin("js")
}

repositories {
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

val webrtcAdapterVersion: String by project
val bootstrapVersion: String by project
val reactBootstrapVersion: String by project
val dmpVersion: String by project

dependencies {
    implementation(libs.kotlinx.html)
    implementation(libs.kotlin.extensions)
    implementation(libs.kotlin.react.react)
    implementation(libs.kotlin.react.dom)
    implementation(libs.kotlin.styled)
    implementation(libs.kotlinx.serialization.json)
    implementation(npm("webrtc-adapter", webrtcAdapterVersion))
    implementation(npm("bootstrap", bootstrapVersion))
    implementation(npm("react-bootstrap", reactBootstrapVersion))
    implementation(npm("diff-match-patch", dmpVersion))
    implementation(projects.chronofold)
    implementation(projects.protocolSignal)

    testImplementation(libs.kotlin.test.js)
    testImplementation(libs.kotest.assertions.core)
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()

            webpackTask {
                cssSupport.enabled = true
                cssSupport.mode = EXTRACT
            }

            runTask {
                cssSupport.enabled = true
                cssSupport.mode = EXTRACT
            }
        }
    }
}
