sealed class AppState

object NotConnectedPage : AppState()

data class LoginPage(val onLogin: (userName: String) -> Unit) : AppState()

data class CallPage(
    val inputText: String,
    val onCall: (userNameToCall: String) -> Unit,
    val onLeave: () -> Unit,
    val onTextChange: (text: String) -> Unit,
) : AppState()
