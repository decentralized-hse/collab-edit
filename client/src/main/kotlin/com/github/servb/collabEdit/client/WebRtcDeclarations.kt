package com.github.servb.collabEdit.client

import org.w3c.dom.ErrorEvent
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import kotlin.js.Promise

external interface webkitRTCIceServer {

    var url: String
}

external interface webkitRTCConfiguration {

    var iceServers: Array<webkitRTCIceServer>?
}

external interface RTCIceCandidate {

    val candidate: String?
    val sdpMid: String?
    val sdpMLineIndex: Int?
    val usernameFragment: String?
}

external interface RTCIceCandidateInit : RTCIceCandidate {

    override var candidate: String?
    override var sdpMid: String?
    override var sdpMLineIndex: Int?
    override var usernameFragment: String?
}

external interface RTCPeerConnectionIceEvent {

    val candidate: RTCIceCandidate?
}

external interface webkitRTCDataChannelInit {

    var reliable: Boolean
}

external interface RTCDataChannel {

    var onerror: ((ErrorEvent) -> dynamic)?
    var onmessage: ((MessageEvent) -> dynamic)?
    var onclose: (() -> dynamic)?

    fun send(data: String)
}

external interface RTCAnswerOptions
external interface RTCOfferOptions

external interface RTCSessionDescription {

    val type: String  // answer, offer, pranswer, or rollback
    val sdp: String
}

external interface RTCSessionDescriptionInit : RTCSessionDescription {

    override var type: String  // answer, offer, pranswer, or rollback
    override var sdp: String
}

external interface B {

    var RtpDataChannels: Boolean
}

external interface A {

    var optional: Array<B>?
}

external class webkitRTCPeerConnection(connection: webkitRTCConfiguration, a: A) {

    var onicecandidate: ((RTCPeerConnectionIceEvent) -> dynamic)?
    var onconnectionstatechange: ((Event) -> dynamic)?

    val iceConnectionState: String

    fun addIceCandidate(candidate: RTCIceCandidateInit)
    fun createDataChannel(label: String, options: webkitRTCDataChannelInit = definedExternally): RTCDataChannel
    fun createAnswer(options: RTCAnswerOptions = definedExternally): Promise<RTCSessionDescription>
    fun createOffer(options: RTCOfferOptions = definedExternally): Promise<RTCSessionDescription>
    fun setLocalDescription(offer: RTCSessionDescription)
    fun setRemoteDescription(offer: RTCSessionDescriptionInit)
    fun close()
}
