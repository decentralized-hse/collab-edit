package com.github.servb.collabEdit.intTest

import io.ktor.server.engine.ApplicationEngine
import java.io.File

object ConnectionUtil {

    private val clientFile = File("client/build/distributions/index.html")

    val clientUrl = "file://${clientFile.absolutePath}"
}

fun ApplicationEngine.stop() = stop(500, 1500)
