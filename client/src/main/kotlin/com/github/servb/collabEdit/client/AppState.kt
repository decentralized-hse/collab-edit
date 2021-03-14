package com.github.servb.collabEdit.client

sealed class AppState

object NotConnectedPage : AppState()

data class LoginPage(val onLogin: (userName: String) -> Unit) : AppState()

data class ConnectionPage(
    val userName: String,
    val onConnect: (userNameToConnect: String) -> Unit,
) : AppState()

data class CollaborationPage(
    val userName: String,
    val otherUserName: String,
    val text: ShownTextRepresentation,
    val onDisconnect: () -> Unit,
    val onTextChange: (newText: ShownTextRepresentation) -> Unit,
) : AppState()
