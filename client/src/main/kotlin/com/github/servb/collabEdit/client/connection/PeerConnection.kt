package com.github.servb.collabEdit.client.connection

import com.github.servb.collabEdit.protocol.signal.CandidateDescription
import com.github.servb.collabEdit.protocol.signal.SessionDescription

abstract class PeerConnection {

    var connectedUser: String? = null

    abstract fun send(data: String)

    abstract fun onLogin(
        onOpen: () -> Unit,
        onMessage: (data: String) -> Unit,
        onClose: () -> Unit,
    )

    abstract fun onConnect()

    abstract fun handleOffer(offer: SessionDescription, connectedUserName: String)
    abstract fun handleAnswer(answer: SessionDescription)
    abstract fun handleCandidate(candidate: CandidateDescription)

    abstract fun close()
}