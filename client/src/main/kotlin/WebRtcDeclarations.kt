import org.w3c.dom.ErrorEvent
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import kotlin.js.Promise

external interface RTCIceServer {

    var urls: Array<String>
    var credential: String?
    var username: String?
}

external interface RTCConfiguration {

    var iceServers: Array<RTCIceServer>?
    var iceTransportPolicy: String?  // relay, all
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

external interface RTCDataChannelInit

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

external class RTCPeerConnection(connection: RTCConfiguration) {

    var onicecandidate: ((RTCPeerConnectionIceEvent) -> dynamic)?
    var onconnectionstatechange: ((Event) -> dynamic)?

    fun addIceCandidate(candidate: RTCIceCandidateInit)
    fun createDataChannel(label: String, options: RTCDataChannelInit = definedExternally): RTCDataChannel
    fun createAnswer(options: RTCAnswerOptions = definedExternally): Promise<RTCSessionDescription>
    fun createOffer(options: RTCOfferOptions = definedExternally): Promise<RTCSessionDescription>
    fun setLocalDescription(offer: RTCSessionDescription)
    fun setRemoteDescription(offer: RTCSessionDescriptionInit)
    fun close()
}
