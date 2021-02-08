package com.github.servb.collabEdit.intTest

import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.conditions.Text
import com.github.servb.collabEdit.server.signal.module
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecRootScope
import io.kotest.core.spec.style.scopes.GivenScope
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun BehaviorSpecRootScope.givenSignalingServer(test: suspend GivenScope.() -> Unit) {
    given("signaling server") {
        val signalServer = embeddedServer(Netty, port = 9090) { module(testing = true) }
        signalServer.start()

        test()

        signalServer.stop(500, 1500)
    }
}

suspend fun GivenScope.andClientTab(test: suspend GivenScope.() -> Unit) {
    and("client tab") {
        open(ConnectionUtil.clientUrl)

        test()
    }
}

suspend fun GivenScope.andAnotherClientTab(test: suspend GivenScope.() -> Unit) {
    and("client tab") {
        executeJavaScript<Unit>("window.open()")
        switchTo().window(1)
        open(ConnectionUtil.clientUrl)

        test()

        switchTo().window(1).close()
    }
}

class Test2Clients : BehaviorSpec({
    givenSignalingServer {
        andClientTab {
            andAnotherClientTab {
                `when`("I login to the first tab") {
                    switchTo().window(0)
                    element("#usernameInput").sendKeys("user1")
                    element("#loginBtn").click()

                    xand("I login to the second tab with the same name") {
                        TODO()
                    }

                    and("I login to the second tab") {
                        switchTo().window(1)
                        element("#usernameInput").sendKeys("user2")
                        element("#loginBtn").click()

                        and("I call the first tab from the second tab") {
                            element("#callToUsernameInput").sendKeys("user1")
                            element("#callBtn").click()

                            and("I send message from the second tab") {
                                val message = "my message, ${System.currentTimeMillis()} ms"

                                element("#msgInput").sendKeys(message)
                                element("#sendMsgBtn").click()

                                val chatEntry = "user2: $message"

                                then("chat in the second tab should contain message") {
                                    element("#chatarea").shouldHave(Text(chatEntry))
                                }

                                then("chat in the first tab should contain message") {
                                    switchTo().window(0)
                                    element("#chatarea").shouldHave(Text(message))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})
