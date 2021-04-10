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
val kotlinReactVersion: String by project
val kotlinStyledVersion: String by project
val reactBootstrapVersion: String by project
val dmpVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")
    implementation("org.jetbrains:kotlin-extensions:$kotlinExtensionsVersion")
    implementation("org.jetbrains:kotlin-react:$kotlinReactVersion")
    implementation("org.jetbrains:kotlin-react-dom:$kotlinReactVersion")
    implementation("org.jetbrains:kotlin-styled:$kotlinStyledVersion")
    implementation(npm("webrtc-adapter", webrtcAdapterVersion))
    implementation(npm("bootstrap", bootstrapVersion))
    implementation(npm("react-bootstrap", reactBootstrapVersion))
    implementation(npm("diff-match-patch", dmpVersion))
    implementation(projects.protocolSignal)
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
