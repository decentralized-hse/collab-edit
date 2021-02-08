package com.github.servb.collabEdit.intTest

import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.conditions.Text
import com.github.servb.collabEdit.server.signal.module
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlin.test.Ignore
import kotlin.test.Test

class Test2Clients {

    @Test
    @Ignore("TODO")
    fun testSameUsernameForbidden() {
        TODO()
    }

    @Test
    fun testSendMessage() {
        val signalServer = embeddedServer(Netty, port = 9090) { module(testing = true) }
        signalServer.start()

        open(ConnectionUtil.clientUrl)

        executeJavaScript<Unit>("window.open()")
        switchTo().window(1)
        open(ConnectionUtil.clientUrl)

        switchTo().window(0)
        element("#usernameInput").sendKeys("user1")
        element("#loginBtn").click()

        switchTo().window(1)
        element("#usernameInput").sendKeys("user2")
        element("#loginBtn").click()

        element("#callToUsernameInput").sendKeys("user1")
        element("#callBtn").click()

        val message = "my message, ${System.currentTimeMillis()} ms"
        val chatEntry = "user2: my message, ${System.currentTimeMillis()} ms"

        element("#msgInput").sendKeys(message)
        element("#sendMsgBtn").click()

        element("#chatarea").shouldHave(Text(message))

        switchTo().window(0)
        element("#chatarea").shouldHave(Text(message))

        signalServer.stop(500, 1500)
    }
}
