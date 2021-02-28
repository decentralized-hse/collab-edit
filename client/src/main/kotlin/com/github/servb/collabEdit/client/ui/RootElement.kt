package com.github.servb.collabEdit.client.ui

import com.github.servb.collabEdit.client.*
import com.github.servb.collabEdit.client.ui.page.collaborationPage
import com.github.servb.collabEdit.client.ui.page.connectionPage
import com.github.servb.collabEdit.client.ui.page.loginPage
import com.github.servb.collabEdit.client.ui.page.notConnectedPage
import react.*

external interface RootElementProps : RProps {

    var appState: AppState
}

val rootElement = functionalComponent<RootElementProps> { props ->
    when (val appState = props.appState) {
        NotConnectedPage -> {
            notConnectedPage()
        }
        is LoginPage -> {
            loginPage {
                this.appState = appState
            }
        }
        is ConnectionPage -> {
            connectionPage {
                this.appState = appState
            }
        }
        is CollaborationPage -> {
            collaborationPage {
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
