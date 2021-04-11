package com.github.servb.collabEdit.intTest

import io.ktor.server.engine.ApplicationEngine

object ConnectionUtil {

    private val clientFile: String get() = System.getProperty("collab.edit.client.file")

    val clientUrl = "file://$clientFile"
}

fun ApplicationEngine.stop() = stop(500, 1500)
