val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val selenideVersion: String by project
val coroutinesVersion: String by project
val kotestVersion: String by project

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

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets[intTestSourceSetName].output.classesDirs
    classpath = sourceSets[intTestSourceSetName].runtimeClasspath

    systemProperties = System.getProperties().map { (k, v) -> k.toString() to v }.toMap()

    shouldRunAfter("test")
    dependsOn(":client:browserProductionWebpack")

    useJUnitPlatform()
}

// todo: uncomment but can fail on CI
//tasks.check { dependsOn(integrationTest) }

dependencies {
    implementation(project(":protocol-signal"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    intTestImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    intTestImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    intTestImplementation("com.codeborne:selenide:$selenideVersion")
    intTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}
