import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("com.github.servb.collabEdit.server.signal.ApplicationKt")
}

val intTestSourceSetName = "intTest"

sourceSets {
    create(intTestSourceSetName) {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val intTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

val webpackTask = project(":client").tasks.getByName<KotlinWebpack>("browserProductionWebpack")

tasks.run.get().apply {
    systemProperties = System.getProperties().map { (k, v) -> k.toString() to v }.toMap() + mapOf(
        "client.dir.path" to webpackTask.destinationDirectory.absolutePath,
    )

    dependsOn(webpackTask)
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    doFirst {
        File("build/reports/tests").mkdirs()  // Selenium doesn't create them itself
    }

    testClassesDirs = sourceSets[intTestSourceSetName].output.classesDirs
    classpath = sourceSets[intTestSourceSetName].runtimeClasspath

    systemProperties = System.getProperties().map { (k, v) -> k.toString() to v }.toMap() + mapOf(
        "client.dir.path" to webpackTask.destinationDirectory.absolutePath,
    )

    shouldRunAfter(tasks.test)
    dependsOn(webpackTask)

    useJUnitPlatform()
}

// todo: uncomment but can fail on CI
//tasks.check { dependsOn(integrationTest) }

dependencies {
    implementation(projects.protocolSignal)
    implementation(libs.ktor.server.netty)
    runtimeOnly(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.websockets)

    intTestImplementation(libs.kotest.runner.junit5)
    intTestImplementation(libs.kotest.assertions.core)
    intTestImplementation(libs.selenide)
    intTestImplementation(libs.kotlinx.coroutines.core)
}
