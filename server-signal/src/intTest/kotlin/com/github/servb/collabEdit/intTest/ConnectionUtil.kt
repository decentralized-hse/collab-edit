package com.github.servb.collabEdit.intTest

import java.io.File

object ConnectionUtil {

    private val clientFile = File("client/build/distributions/index.html")

    val clientUrl = "file://${clientFile.absolutePath}"
}
