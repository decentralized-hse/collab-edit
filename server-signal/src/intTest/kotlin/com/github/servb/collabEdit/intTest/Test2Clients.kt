package com.github.servb.collabEdit.intTest

import com.codeborne.selenide.Condition.exactValue
import com.codeborne.selenide.SelenideDriver
import com.github.servb.collabEdit.server.signal.module
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecRootScope
import io.kotest.core.spec.style.scopes.GivenScope
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.delay
import org.openqa.selenium.Dimension
import org.openqa.selenium.Keys
import org.openqa.selenium.Point

private var givenSignalingServerNextId = 0

fun BehaviorSpecRootScope.givenSignalingServer(test: suspend GivenScope.(server: ApplicationEngine) -> Unit) {
    given("signaling server (${givenSignalingServerNextId++})") {
        val signalServer = embeddedServer(Netty, port = 9090) { module(testing = true) }
        signalServer.start()

        test(signalServer)  // signaling server can be stopped there

        signalServer.stop()  // if it wasn't stopped, stop it here
    }
}

enum class Tab(val posX: Int, val posY: Int) {

    FIRST(100, 50),
    SECOND(1000, 50),
}

private val tabs = mutableMapOf<Tab, SelenideDriver>()

suspend fun GivenScope.andClientTab(tab: Tab, test: suspend GivenScope.(LoginPage) -> Unit) {
    and("client tab ($tab)") {
        val driver = tabs.getOrPut(tab) {
            createDriver().also {
                it.open("about:blank")  // open before changing window to avoid exceptions
                it.webDriver.manage().window().apply {
                    position = Point(tab.posX, tab.posY)
                    size = Dimension(400, 400)
                }
            }
        }

        val client = openClient(driver)
        test(client)

        driver.open("about:blank")
    }
}

class Test2Clients : BehaviorSpec({
    // test logging in with the same name
    givenSignalingServer {
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab with the same name") {
                        loginPage2.loginAs(connectionPage1.userName)

                        then("error should be shown") {
                            loginPage2.driver.switchTo().alert().accept()
                        }
                    }
                }
            }
        }
    }

    // test text sending
    givenSignalingServer {
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                            val collaborationPage1 = connectionPage1 shouldBeConnectedTo collaborationPage2.userName

                            and("I input text from the second tab") {
                                val text1 = "my message, ${System.currentTimeMillis()} ms"

                                collaborationPage2.input(text1)

                                then("chat in the second tab should contain text") {
                                    collaborationPage2.text.shouldHave(exactValue(text1))
                                }

                                then("chat in the first tab should contain text") {
                                    collaborationPage1.text.shouldHave(exactValue("$text1|"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // test text sending with stopped signaling server
    givenSignalingServer { server ->
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                            val collaborationPage1 = connectionPage1 shouldBeConnectedTo collaborationPage2.userName

                            and("The signaling server stops") {
                                server.stop()
                                delay(1500)

                                and("I input text from the second tab") {
                                    val text1 = "my message, ${System.currentTimeMillis()} ms"

                                    collaborationPage2.input(text1)

                                    then("chat in the second tab should contain text") {
                                        collaborationPage2.text.shouldHave(exactValue(text1))
                                    }

                                    then("chat in the first tab should contain text") {
                                        collaborationPage1.text.shouldHave(exactValue("$text1|"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // test text addition from both tabs
    givenSignalingServer {
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                            val collaborationPage1 = connectionPage1 shouldBeConnectedTo collaborationPage2.userName

                            and("I input text from the second tab") {
                                val text1 = "my message, ${System.currentTimeMillis()} ms"

                                collaborationPage2.input(text1)

                                and("I add text from the first tab") {
                                    val text2 = "\nmy message 2, ${System.currentTimeMillis()} ms"

                                    collaborationPage1.input(text2)

                                    then("chat in the second tab should contain old text and appended text") {
                                        collaborationPage2.text.shouldHave(exactValue("$text1$text2|"))
                                    }

                                    then("chat in the first tab should contain old text and appended text") {
                                        collaborationPage1.text.shouldHave(exactValue("$text1|$text2"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // test reconnection
    givenSignalingServer {
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)

                            and("I disconnect on the second tab") {
                                collaborationPage2.disconnect()

                                and("I call the first tab from the second tab") {
                                    @Suppress("NAME_SHADOWING")
                                    val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                                    val collaborationPage1 =
                                        connectionPage1 shouldBeConnectedTo collaborationPage2.userName

                                    and("I input text from the second tab") {
                                        val text1 = "my message, ${System.currentTimeMillis()} ms"

                                        collaborationPage2.input(text1)

                                        then("chat in the second tab should contain text") {
                                            collaborationPage2.text.shouldHave(exactValue(text1))
                                        }

                                        then("chat in the first tab should contain text") {
                                            collaborationPage1.text.shouldHave(exactValue("$text1|"))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // test text cursor moving on one side seen from other side
    givenSignalingServer {
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                            val collaborationPage1 = connectionPage1 shouldBeConnectedTo collaborationPage2.userName

                            and("I input text from the second tab") {
                                val text1 = "my message, ${System.currentTimeMillis()} ms"

                                collaborationPage2.input(text1)

                                and("I place cursor to the start of textarea") {
                                    collaborationPage2.input(Keys.PAGE_UP)

                                    then("chat in the second tab should contain correct text") {
                                        collaborationPage2.text.shouldHave(exactValue(text1))
                                    }

                                    then("chat in the first tab should contain correct text") {
                                        collaborationPage1.text.shouldHave(exactValue("|$text1"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // test text addition from both tabs with cursor sync
    givenSignalingServer {
        andClientTab(Tab.FIRST) { loginPage1 ->
            andClientTab(Tab.SECOND) { loginPage2 ->
                `when`("I login to the first tab") {
                    val connectionPage1 = loginPage1.loginAs("user1")

                    and("I login to the second tab") {
                        val connectionPage2 = loginPage2.loginAs("user2")

                        and("I call the first tab from the second tab") {
                            val collaborationPage2 = connectionPage2.connect(connectionPage1.userName)
                            val collaborationPage1 = connectionPage1 shouldBeConnectedTo collaborationPage2.userName

                            and("I input text from the second tab") {
                                val text1 = "my message, ${System.currentTimeMillis()} ms"

                                collaborationPage2.input(text1)

                                and("I place cursor to the start of textarea") {
                                    collaborationPage2.input(Keys.PAGE_UP)

                                    and("I add text from the first tab") {
                                        val text2 = "\nmy message 2, ${System.currentTimeMillis()} ms"

                                        collaborationPage1.input(text2)

                                        and("I input text from the second tab") {
                                            val text3 = "my message 3, ${System.currentTimeMillis()} ms"

                                            collaborationPage2.input(text3)

                                            then("chat in the second tab should contain correct text") {
                                                collaborationPage2.text.shouldHave(exactValue("$text3$text1$text2|"))
                                            }

                                            then("chat in the first tab should contain correct text") {
                                                collaborationPage1.text.shouldHave(exactValue("$text3|$text1$text2"))
                                            }
                                        }
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
