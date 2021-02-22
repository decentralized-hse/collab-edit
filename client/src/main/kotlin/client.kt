import com.github.servb.collabEdit.protocol.signal.CandidateDescription
import com.github.servb.collabEdit.protocol.signal.SessionDescription
import com.github.servb.collabEdit.protocol.signal.ToClientMessage
import com.github.servb.collabEdit.protocol.signal.ToServerMessage
import kotlinext.js.jsObject
import kotlinext.js.require
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.WebSocket
import react.dom.render
import ui.rootElement

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

fun onCall(userNameToCall: String) {
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

fun onLeave() {
    send(ToServerMessage.Leave(connectedUser!!))

    handleLeave()
}

fun onTextChange(text: String) {
    render(CallPage(text, ::onCall, ::onLeave, ::onTextChange))
    dataChannel.send(text)
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
        render(CallPage("", ::onCall, ::onLeave, ::onTextChange))

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

        dataChannel.onerror = {
            console.log("Ooops...error:", it)
        }

        dataChannel.onmessage = {
            render(CallPage(it.data as String, ::onCall, ::onLeave, ::onTextChange))
        }

        dataChannel.onclose = {
            console.log("data channel is closed")
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
