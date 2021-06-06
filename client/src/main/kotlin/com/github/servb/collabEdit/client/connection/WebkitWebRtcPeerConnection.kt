package com.github.servb.collabEdit.client.connection

import com.github.servb.collabEdit.client.RTCDataChannel
import com.github.servb.collabEdit.client.sendToSignal
import com.github.servb.collabEdit.client.webkitRTCConfiguration
import com.github.servb.collabEdit.client.webkitRTCPeerConnection
import com.github.servb.collabEdit.protocol.signal.CandidateDescription
import com.github.servb.collabEdit.protocol.signal.SessionDescription
import com.github.servb.collabEdit.protocol.signal.ToServerMessage
import kotlinext.js.jsObject

class WebkitWebRtcPeerConnection : PeerConnection() {

    private lateinit var yourConn: webkitRTCPeerConnection
    private lateinit var dataChannel: RTCDataChannel

    override fun send(data: String) {
        val safeSizeToSendViaWebRtc =
            100  // can't send everything right away, so let's split the message: https://developer.mozilla.org/en-US/docs/Web/API/WebRTC_API/Using_data_channels#understanding_message_size_limits
        data.chunked(safeSizeToSendViaWebRtc).forEach {
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
    }

    override fun onLogin(
        onOpen: () -> Unit,
        onMessage: (data: String) -> Unit,
        onClose: () -> Unit,
    ) {
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
                sendToSignal(
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

        dataChannel.onopen = { onOpen() }

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

                onMessage(fullData)
            }
        }

        dataChannel.onclose = {
            console.log("data channel is closed")
            onClose()
        }
    }

    override fun onConnect() {
        yourConn.createOffer()
            .then { offer ->
                console.log("created offer:", offer)
                sendToSignal(
                    ToServerMessage.Offer(
                        SessionDescription(sdp = offer.sdp, type = offer.type),
                        connectedUser!!
                    )
                )
                yourConn.setLocalDescription(offer)
            }
            .catch {
                console.error("error when creating offer", it)
            }
    }

    override fun handleOffer(offer: SessionDescription, connectedUserName: String) {
        connectedUser = connectedUserName
        yourConn.setRemoteDescription(jsObject {
            type = offer.type
            sdp = offer.sdp
        })

        yourConn.createAnswer()
            .then { answer ->
                yourConn.setLocalDescription(answer)
                sendToSignal(
                    ToServerMessage.Answer(
                        SessionDescription(sdp = answer.sdp, type = answer.type),
                        connectedUser!!
                    )
                )
            }
            .catch {
                console.error("Error when creating an answer", it)
            }
    }

    override fun handleAnswer(answer: SessionDescription) {
        yourConn.setRemoteDescription(jsObject {
            sdp = answer.sdp
            type = answer.type
        })
    }

    override fun handleCandidate(candidate: CandidateDescription) {
        yourConn.addIceCandidate(jsObject {
            this.candidate = candidate.candidate
            this.sdpMLineIndex = candidate.sdpMLineIndex
            this.sdpMid = candidate.sdpMid
            this.usernameFragment = candidate.usernameFragment
        })
    }

    override fun close() {
        connectedUser = null
        yourConn.close()
        yourConn.onicecandidate = null
    }
}