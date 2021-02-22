package ui

import CallPage
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*

external interface CallPageProps : RProps {

    var appState: CallPage
}

val callPage = functionalComponent<CallPageProps> { props ->
    val (userNameToCall, setUserNameToCall) = useState("")

    div("call-page container") {
        attrs {
            id = "callPage"
        }

        div("row") {
            div("col-md-4 col-md-offset-4 text-center") {
                div("panel panel-primary") {
                    div("panel-heading") {
                        +"collab-edit"
                    }
                }
            }
        }

        div("row text-center form-group") {
            div("col-md-12") {
                input(type = InputType.text) {
                    attrs {
                        id = "callToUsernameInput"
                        placeholder = "username to call"
                        onInputFunction = {
                            setUserNameToCall((it.target as HTMLInputElement).value)
                        }
                        value = userNameToCall
                    }
                }
                button(classes = "btn-success btn") {
                    attrs {
                        id = "callBtn"
                        onClickFunction = {
                            props.appState.onCall(userNameToCall)
                        }
                    }

                    +"Call"
                }
                button(classes = "btn-danger btn") {
                    attrs {
                        id = "hangUpBtn"
                        onClickFunction = {
                            props.appState.onLeave()
                        }
                    }

                    +"Hang Up"
                }
            }
        }

        div("row text-center") {
            div("col-md-12") {
                textArea {
                    attrs {
                        id = "text"
                        placeholder = "Collaborate here!"
                        onChangeFunction = {
                            val text = (it.target as HTMLTextAreaElement).value
                            props.appState.onTextChange(text)
                        }
                        value = props.appState.inputText
                    }
                }
            }
        }
    }
}

fun RBuilder.callPage(handler: CallPageProps.() -> Unit): ReactElement {
    return child(callPage) {
        attrs.handler()
    }
}
