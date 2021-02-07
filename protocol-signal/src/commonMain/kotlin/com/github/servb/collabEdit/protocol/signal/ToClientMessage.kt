package com.github.servb.collabEdit.protocol.signal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed class ToClientMessage {

    @Serializable
    @SerialName("login")
    data class Login(val success: Boolean) : ToClientMessage()

    @Serializable
    @SerialName("offer")
    data class Offer(val offer: SessionDescription, val name: String) : ToClientMessage()

    @Serializable
    @SerialName("answer")
    data class Answer(val answer: SessionDescription) : ToClientMessage()

    @Serializable
    @SerialName("candidate")
    data class Candidate(val candidate: CandidateDescription) : ToClientMessage()

    @Serializable
    @SerialName("leave")
    object Leave : ToClientMessage()

    companion object {

        private val json = Json.Default

        fun decode(message: String): ToClientMessage = json.decodeFromString(ToClientMessage.serializer(), message)
        fun encode(message: ToClientMessage): String = json.encodeToString(ToClientMessage.serializer(), message)
    }
}
