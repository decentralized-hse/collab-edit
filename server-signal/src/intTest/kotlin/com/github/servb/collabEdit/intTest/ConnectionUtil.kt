package com.github.servb.collabEdit.intTest

import io.ktor.server.engine.ApplicationEngine

object ConnectionUtil {

    const val port = 9091

    const val clientUrl = "http://localhost:$port"
}

fun ApplicationEngine.stop() = stop(500, 1500)
