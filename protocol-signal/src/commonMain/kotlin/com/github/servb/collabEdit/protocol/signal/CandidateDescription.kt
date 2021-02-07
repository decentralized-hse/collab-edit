package com.github.servb.collabEdit.protocol.signal

import kotlinx.serialization.Serializable

@Serializable
data class CandidateDescription(
    val candidate: String? = null,
    val sdpMid: String? = null,
    val sdpMLineIndex: Int? = null,
    val usernameFragment: String? = null
)
