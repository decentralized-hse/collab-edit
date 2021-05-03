package com.github.servb.collabEdit.server.signal

import com.github.servb.collabEdit.protocol.signal.ToClientMessage
import com.github.servb.collabEdit.protocol.signal.ToServerMessage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.combineSafe
import io.ktor.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.slf4j.event.Level
import java.io.File
import java.time.Duration
import kotlin.collections.set

fun main() {
    val port = System.getProperty("signal.port")?.toIntOrNull() ?: 9090
    println("Starting the signaling server on port $port...")

    embeddedServer(Netty, port = port) {
        module()
    }.start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(StatusPages) {
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    class UserInfo(
        val connection: DefaultWebSocketServerSession,
        var name: String?,
        var otherName: String? = null,
    ) {

        suspend fun send(message: ToClientMessage) {
            connection.send(ToClientMessage.encode(message))
        }
    }

    val users = mutableMapOf<String, UserInfo>()

    routing {
        static("/") {
            val staticRoot = File(System.getProperty("client.dir.path"))
            files(staticRoot)
            default(staticRoot.combineSafe("index.html"))
        }

        webSocket("/") {
            log.info("User connected")

            var thisName: String? = null

            try {
                while (true) {
                    val frame = incoming.receive()
                    check(frame.fin) { "Not supporting split frames" }
                    val text = (frame as? Frame.Text)?.readText()
                    log.info("received from $thisName: $text")
                    val message = text?.let { ToServerMessage.decode(it) }

                    when (message) {
                        is ToServerMessage.Login -> {
                            log.info("User logged: ${message.name}")
                            thisName = message.name
                            val user = users[message.name]
                            val newUser = UserInfo(this, message.name)
                            if (user != null) {
                                newUser.send(ToClientMessage.Login(success = false))
                            } else {
                                users[message.name] = newUser
                                newUser.send(ToClientMessage.Login(success = true))
                            }
                        }
                        is ToServerMessage.Offer -> {
                            log.info("Sending offer to: ${message.name}")
                            val thisUser = users.getValue(thisName!!)
                            val otherUser = users[message.name]

                            if (otherUser != null) {
                                thisUser.otherName = message.name
                                otherUser.send(ToClientMessage.Offer(offer = message.offer, name = thisName))
                            }
                        }
                        is ToServerMessage.Answer -> {
                            log.info("Sending answer to: ${message.name}")
                            val thisUser = users.getValue(thisName!!)
                            val otherUser = users[message.name]

                            if (otherUser != null) {
                                thisUser.otherName = message.name
                                otherUser.send(ToClientMessage.Answer(answer = message.answer))
                            }
                        }
                        is ToServerMessage.Candidate -> {
                            log.info("Sending candidate to: ${message.name}")
                            val otherUser = users[message.name]

                            if (otherUser != null) {
                                otherUser.send(ToClientMessage.Candidate(candidate = message.candidate))
                            }
                        }
                        is ToServerMessage.Leave -> {
                            log.info("Disconnecting from ${message.name}")
                            val otherUser = users[message.name]

                            if (otherUser != null) {
                                otherUser.otherName = null
                                otherUser.send(ToClientMessage.Leave)
                            }
                        }
                        else -> {
                            log.warn("Bad message: $frame")
                        }
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                if (thisName != null) {
                    val thisUser = users.remove(thisName)!!

                    if (thisUser.otherName != null) {
                        log.info("Disconnecting from ${thisUser.otherName}")
                        val otherUser = users[thisUser.otherName]
                        if (otherUser != null) {
                            otherUser.otherName = null
                            otherUser.send(ToClientMessage.Leave)
                        }
                    }
                }
            }
        }
    }
}
