package com.github.servb.collabEdit.protocol.signal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed class ToServerMessage {

    @Serializable
    @SerialName("login")
    data class Login(val name: String) : ToServerMessage()

    @Serializable
    @SerialName("offer")
    data class Offer(val offer: SessionDescription, val name: String) : ToServerMessage()

    @Serializable
    @SerialName("answer")
    data class Answer(val answer: SessionDescription, val name: String) : ToServerMessage()

    @Serializable
    @SerialName("candidate")
    data class Candidate(val candidate: CandidateDescription, val name: String) : ToServerMessage()

    @Serializable
    @SerialName("leave")
    data class Leave(val name: String) : ToServerMessage()

    companion object {

        private val json = Json.Default

        fun decode(message: String): ToServerMessage = json.decodeFromString(ToServerMessage.serializer(), message)
        fun encode(message: ToServerMessage): String = json.encodeToString(ToServerMessage.serializer(), message)
    }
}
