package com.github.servb.collabEdit.client

import com.github.servb.collabEdit.chronofold.*
import com.github.servb.collabEdit.client.connection.PeerConnection
import com.github.servb.collabEdit.client.connection.WebSocketPeerConnection
import com.github.servb.collabEdit.client.connection.WebkitWebRtcPeerConnection
import com.github.servb.collabEdit.client.ui.rootElement
import com.github.servb.collabEdit.protocol.signal.CandidateDescription
import com.github.servb.collabEdit.protocol.signal.SessionDescription
import com.github.servb.collabEdit.protocol.signal.ToClientMessage
import com.github.servb.collabEdit.protocol.signal.ToServerMessage
import kotlinext.js.require
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.w3c.dom.WebSocket
import org.w3c.dom.url.URL
import react.dom.render

private var name: String? = null
private var peerConnection: PeerConnection? = null

private val signalUrl = URL(window.location.href).searchParams.get("ws")?.let { "ws://$it" }
    ?: URL(window.location.href).apply { protocol = protocol.replace("http", "ws") }.href

private val signalConnection = WebSocket(signalUrl)

fun sendToSignal(msg: ToServerMessage) {
    signalConnection.send(ToServerMessage.encode(msg))
}

private fun render(appState: AppState) {
    render(document.getElementById("root")) {
        rootElement {
            this.appState = appState
        }
    }
}

private fun onLogin(userName: String, useWebRtc: Boolean) {
    name = userName
    peerConnection = when (useWebRtc) {
        true -> WebkitWebRtcPeerConnection()
        false -> WebSocketPeerConnection(signalConnection)
    }
    if (name!!.isNotEmpty()) {
        sendToSignal(ToServerMessage.Login(name!!))
    }
}

private fun onConnect(userNameToCall: String) {
    if (userNameToCall.isNotEmpty()) {
        peerConnection!!.connectedUser = userNameToCall
        peerConnection!!.onConnect()
    }
}

private fun onDisconnect() {
    sendToSignal(ToServerMessage.Leave(peerConnection!!.connectedUser!!))

    handleLeave()
}

private lateinit var ct: CausalTree
private lateinit var chronofold: Chronofold

private fun onTextChange(text: ShownTextRepresentation) {
    render(
        CollaborationPage(
            userName = name!!,
            otherUserName = peerConnection!!.connectedUser!!,
            text = text,
            onDisconnect = ::onDisconnect,
            onTextChange = ::onTextChange,
        )
    )
    val sentText = text.toSent(name!!, peerConnection!!.connectedUser!!).sentText
    val ops = diff(sentText, chronofold, ct, name!!)
    ops.applyTo(ct, chronofold)
    val textToSend = json.encodeToString(ops)
    console.log("going to send ${textToSend.length} chars:", ops)
    peerConnection!!.send(textToSend)
    console.log("sent successfully")
}

private fun onMessage(data: String) {
    try {
        val ops = json.decodeFromString<List<Operation>>(data)
        console.log("received ops:", ops)
        ops.applyTo(ct, chronofold)
        val newText = chronofold.getString()
        console.log("new text:", newText)

        val shownText =
            SentTextRepresentation(newText).toShown(name!!, peerConnection!!.connectedUser!!)

        render(
            CollaborationPage(
                userName = name!!,
                otherUserName = peerConnection!!.connectedUser!!,
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

fun main() {
    require("bootstrap/dist/css/bootstrap.min.css")

    render(NotConnectedPage)

    signalConnection.onopen = {
        console.log("Connected to the signaling server")
        render(LoginPage(::onLogin))
    }

    signalConnection.onmessage = {
        console.log("Got message", it.data)
        val data = ToClientMessage.decode(it.data as String)

        when (data) {
            is ToClientMessage.Login -> handleLogin(data.success)
            is ToClientMessage.Offer -> handleOffer(data.offer, data.name)
            is ToClientMessage.Answer -> handleAnswer(data.answer)
            is ToClientMessage.Candidate -> handleCandidate(data.candidate)
            is ToClientMessage.Leave -> handleLeave()
            is ToClientMessage.Connect -> {
                peerConnection!!.connectedUser = data.name
                onOpen()
            }
            is ToClientMessage.Message -> onMessage(data.data)
        }
    }

    signalConnection.onerror = {
        console.log("Got error", it)
    }
}

private fun handleLogin(success: Boolean) {
    if (!success) {
        window.alert("Can't use this username, maybe it's already taken. Try another one")
    } else {
        val (newCt, newChronofold) = createInitialData("", "rootAuthor")
        ct = newCt
        chronofold = newChronofold

        render(ConnectionPage(userName = name!!, onConnect = ::onConnect))

        peerConnection!!.onLogin(
            onOpen = ::onOpen,
            onMessage = ::onMessage,
            onClose = { handleLogin(success = true) },
        )
    }
}

private fun onOpen() {
    render(
        CollaborationPage(
            userName = name!!,
            otherUserName = peerConnection!!.connectedUser!!,
            text = ShownTextRepresentation(null, ""),
            onDisconnect = ::onDisconnect,
            onTextChange = ::onTextChange,
        )
    )
}

private fun handleOffer(offer: SessionDescription, connectedUserName: String) {
    peerConnection!!.handleOffer(offer, connectedUserName)
}

private fun handleAnswer(answer: SessionDescription) {
    peerConnection!!.handleAnswer(answer)
}

private fun handleCandidate(candidate: CandidateDescription) {
    peerConnection!!.handleCandidate(candidate)
}

private fun handleLeave() {
    peerConnection!!.close()
    peerConnection = null
}
