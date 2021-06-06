package com.github.servb.collabEdit.client.connection

import com.github.servb.collabEdit.client.sendToSignal
import com.github.servb.collabEdit.protocol.signal.CandidateDescription
import com.github.servb.collabEdit.protocol.signal.SessionDescription
import com.github.servb.collabEdit.protocol.signal.ToServerMessage
import org.w3c.dom.WebSocket

class WebSocketPeerConnection(private val signalConnection: WebSocket) : PeerConnection() {

    lateinit var onOpen: () -> Unit

    override fun send(data: String) {
        sendToSignal(ToServerMessage.Message(data = data, name = connectedUser!!))
    }

    override fun onLogin(onOpen: () -> Unit, onMessage: (data: String) -> Unit, onClose: () -> Unit) {
        signalConnection.onclose = {
            onClose()
            signalConnection.onclose?.invoke(it)
        }
        this.onOpen = onOpen
    }

    override fun onConnect() {
        sendToSignal(ToServerMessage.Connect(connectedUser!!))
        onOpen()
    }

    override fun handleOffer(offer: SessionDescription, connectedUserName: String) {
        // n/a for ws
    }

    override fun handleAnswer(answer: SessionDescription) {
        // n/a for ws
    }

    override fun handleCandidate(candidate: CandidateDescription) {
        // n/a for ws
    }

    override fun close() {
        // n/a for ws
    }
}