package com.github.servb.collabEdit.intTest

import com.codeborne.selenide.Condition.exactValue
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
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab with the same name") {
                        loginPage2.loginAs(connectionPage1.userName)

                        then("error should be shown") {
                            loginPage2.driver.switchTo().alert().accept()
                        }
                    }

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                            val collaborationPage1 = connectionPage1.asConnectedTo(collaborationPage2.userName)

                            and("I input text from the second tab") {
                                val text1 = "my message, ${System.currentTimeMillis()} ms"

                                collaborationPage2.input(text1)

                                then("chat in the second tab should contain text") {
                                    collaborationPage2.text.shouldHave(exactValue(text1))
                                }

                                then("chat in the first tab should contain text") {
                                    collaborationPage1.text.shouldHave(exactValue(text1))
                                }

                                and("I add text from the first tab") {
                                    val text2 = "\nmy message 2, ${System.currentTimeMillis()} ms"

                                    collaborationPage1.input(text2)

                                    then("chat in the second tab should contain old text and appended text") {
                                        collaborationPage2.text.shouldHave(exactValue(text1 + text2))
                                    }

                                    then("chat in the first tab should contain old text and appended text") {
                                        collaborationPage1.text.shouldHave(exactValue(text1 + text2))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})
