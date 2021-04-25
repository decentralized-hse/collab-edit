package com.github.servb.collabEdit.client

import com.github.servb.collabEdit.chronofold.*
import com.github.servb.collabEdit.client.ui.rootElement
import com.github.servb.collabEdit.protocol.signal.CandidateDescription
import com.github.servb.collabEdit.protocol.signal.SessionDescription
import com.github.servb.collabEdit.protocol.signal.ToClientMessage
import com.github.servb.collabEdit.protocol.signal.ToServerMessage
import kotlinext.js.jsObject
import kotlinext.js.require
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.w3c.dom.WebSocket
import react.dom.render

var name: String? = null
var connectedUser: String? = null

lateinit var yourConn: webkitRTCPeerConnection
lateinit var dataChannel: RTCDataChannel

val conn = WebSocket("ws://localhost:9090")

fun send(msg: ToServerMessage) {
    conn.send(ToServerMessage.encode(msg))
}

fun render(appState: AppState) {
    render(document.getElementById("root")) {
        rootElement {
            this.appState = appState
        }
    }
}

fun onLogin(userName: String) {
    name = userName
    if (name!!.isNotEmpty()) {
        send(ToServerMessage.Login(name!!))
    }
}

fun onConnect(userNameToCall: String) {
    if (userNameToCall.isNotEmpty()) {
        connectedUser = userNameToCall
        yourConn.createOffer()
            .then { offer ->
                console.log("created offer:", offer)

                send(ToServerMessage.Offer(SessionDescription(sdp = offer.sdp, type = offer.type), connectedUser!!))

                yourConn.setLocalDescription(offer)
            }
            .catch {
                console.error("error when creating offer", it)
            }
    }
}

fun onDisconnect() {
    send(ToServerMessage.Leave(connectedUser!!))

    handleLeave()
}

private lateinit var ct: CausalTree
private lateinit var chronofold: Chronofold

fun onTextChange(text: ShownTextRepresentation) {
    render(
        CollaborationPage(
            userName = name!!,
            otherUserName = connectedUser!!,
            text = text,
            onDisconnect = ::onDisconnect,
            onTextChange = ::onTextChange,
        )
    )
    val sentText = text.toSent(name!!, connectedUser!!).sentText
    val ops = diff(sentText, chronofold, ct, name!!)
    ops.applyTo(ct, chronofold)
    val textToSend = json.encodeToString(ops)
    console.log("going to send ${textToSend.length} chars:", ops)
    val safeSizeToSendViaWebRtc =
        100  // can't send everything right away, so let's split the message: https://developer.mozilla.org/en-US/docs/Web/API/WebRTC_API/Using_data_channels#understanding_message_size_limits
    textToSend.chunked(safeSizeToSendViaWebRtc).forEach {
        console.log("sending part", it)
        while (true) {
            try {
                dataChannel.send(it)
                break
            } catch (t: dynamic) {
                console.error("can't send data, trying to resend", it, t)
                // todo: add delay
            }
        }
    }
    console.log("sent successfully")
}

fun main() {
    require("bootstrap/dist/css/bootstrap.min.css")

    render(NotConnectedPage)

    conn.onopen = {
        console.log("Connected to the signaling server")
        render(LoginPage(::onLogin))
    }

    conn.onmessage = {
        console.log("Got message", it.data)
        val data = ToClientMessage.decode(it.data as String)

        when (data) {
            is ToClientMessage.Login -> handleLogin(data.success)
            is ToClientMessage.Offer -> handleOffer(data.offer, data.name)
            is ToClientMessage.Answer -> handleAnswer(data.answer)
            is ToClientMessage.Candidate -> handleCandidate(data.candidate)
            is ToClientMessage.Leave -> handleLeave()
        }
    }

    conn.onerror = {
        console.log("Got error", it)
    }
}

fun handleLogin(success: Boolean) {
    if (!success) {
        window.alert("Ooops...try a different username")
    } else {
        val (newCt, newChronofold) = createInitialData("", "rootAuthor")
        ct = newCt
        chronofold = newChronofold

        render(ConnectionPage(userName = name!!, onConnect = ::onConnect))

        val configuration = jsObject<webkitRTCConfiguration> {
            iceServers = arrayOf(
                jsObject {
                    url = "stun:stun1.l.google.com:19302"
                },
            )
        }

        yourConn =
            webkitRTCPeerConnection(configuration, jsObject { optional = arrayOf(jsObject { RtpDataChannels = true }) })

        yourConn.onicecandidate = {
            console.log("yourConn.onicecandidate", it)
            if (it.candidate != null) {
                send(
                    ToServerMessage.Candidate(
                        CandidateDescription(
                            candidate = it.candidate!!.candidate,
                            sdpMid = it.candidate!!.sdpMid,
                            sdpMLineIndex = it.candidate!!.sdpMLineIndex,
                            usernameFragment = it.candidate!!.usernameFragment,
                        ),
                        connectedUser!!
                    )
                )
            }
        }

        yourConn.onconnectionstatechange = {
            console.log("onconnectionstatechange", yourConn.iceConnectionState, it)
        }

        dataChannel = yourConn.createDataChannel("channel1", jsObject { reliable = true })

        dataChannel.onopen = {
            render(
                CollaborationPage(
                    userName = name!!,
                    otherUserName = connectedUser!!,
                    text = ShownTextRepresentation(null, ""),
                    onDisconnect = ::onDisconnect,
                    onTextChange = ::onTextChange,
                )
            )
        }

        dataChannel.onerror = {
            console.log("Ooops...error:", it)
        }

        val received = mutableListOf<String>()

        dataChannel.onmessage = {
            val data = it.data as String
            received.add(data)
            console.info("received part", data)

            if (data.endsWith(']')) {
                val fullData = received.joinToString("")
                received.clear()

                try {
                    val ops = json.decodeFromString<List<Operation>>(fullData)
                    console.log("received ops:", ops)
                    ops.applyTo(ct, chronofold)
                    val newText = chronofold.getString()
                    console.log("new text:", newText)

                    val shownText = SentTextRepresentation(newText).toShown(name!!, connectedUser!!)

                    render(
                        CollaborationPage(
                            userName = name!!,
                            otherUserName = connectedUser!!,
                            text = shownText,
                            onDisconnect = ::onDisconnect,
                            onTextChange = ::onTextChange,
                        )
                    )
                } catch (t: dynamic) {
                    window.alert("bad data received, can't continue (see error in console)")
                    console.error("bad data received", t)
                }
            }
        }

        dataChannel.onclose = {
            console.log("data channel is closed")
            handleLogin(success = true)
        }
    }
}

fun handleOffer(offer: SessionDescription, connectedUserName: String) {
    connectedUser = connectedUserName
    yourConn.setRemoteDescription(jsObject {
        type = offer.type
        sdp = offer.sdp
    })

    yourConn.createAnswer()
        .then { answer ->
            yourConn.setLocalDescription(answer)
            send(ToServerMessage.Answer(SessionDescription(sdp = answer.sdp, type = answer.type), connectedUser!!))
        }
        .catch {
            console.error("Error when creating an answer", it)
        }
}

fun handleAnswer(answer: SessionDescription) {
    yourConn.setRemoteDescription(jsObject {
        sdp = answer.sdp
        type = answer.type
    })
}

fun handleCandidate(candidate: CandidateDescription) {
    yourConn.addIceCandidate(jsObject {
        this.candidate = candidate.candidate
        this.sdpMLineIndex = candidate.sdpMLineIndex
        this.sdpMid = candidate.sdpMid
        this.usernameFragment = candidate.usernameFragment
    })
}

fun handleLeave() {
    connectedUser = null
    yourConn.close()
    yourConn.onicecandidate = null
}
