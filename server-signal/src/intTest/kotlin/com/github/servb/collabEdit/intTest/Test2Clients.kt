package com.github.servb.collabEdit.intTest

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

suspend fun GivenScope.andClientTab(test: suspend GivenScope.(LoginPage) -> Unit) {
    and("client tab") {
        val client = openClient()

        test(client)

        client.driver.close()
    }
}

class Test2Clients : BehaviorSpec({
    givenSignalingServer {
        andClientTab { loginPage1 ->
            andClientTab { loginPage2 ->
                `when`("I login to the first tab") {
                    val callPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab with the same name") {
                        loginPage2.loginAs(callPage1.userName)

                        then("error should be shown") {
                            loginPage2.driver.switchTo().alert().accept()
                        }
                    }

                    and("I login to the second tab") {
                        val callPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            callPage2.call(callPage1.userName)

                            and("I send message from the second tab") {
                                val message = "my message, ${System.currentTimeMillis()} ms"

                                callPage2.send(message)

                                val chatEntry = "${callPage2.userName}: $message"

                                then("chat in the second tab should contain message") {
                                    callPage2.chatArea.shouldHave(Text(chatEntry))
                                }

                                then("chat in the first tab should contain message") {
                                    callPage1.chatArea.shouldHave(Text(chatEntry))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})
