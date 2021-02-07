package com.github.servb.collabEdit.protocol.signal

import kotlinx.serialization.Serializable

@Serializable
data class SessionDescription(val sdp: String, val type: String)
