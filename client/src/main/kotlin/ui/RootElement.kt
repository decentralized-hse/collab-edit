package ui

import AppState
import CallPage
import LoginPage
import NotConnectedPage
import react.*

external interface RootElementProps : RProps {

    var appState: AppState
}

val rootElement = functionalComponent<RootElementProps> { props ->

    when (val appState = props.appState) {
        NotConnectedPage -> {
        }
        is LoginPage -> {
            loginPage {
                this.appState = appState
            }
        }
        is CallPage -> {
            callPage {
                this.appState = appState
            }
        }
    }
}

fun RBuilder.rootElement(handler: RootElementProps.() -> Unit): ReactElement {
    return child(rootElement) {
        attrs.handler()
    }
}
