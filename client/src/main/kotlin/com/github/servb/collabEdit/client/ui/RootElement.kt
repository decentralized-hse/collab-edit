package com.github.servb.collabEdit.client.ui

import com.github.servb.collabEdit.client.AppState
import com.github.servb.collabEdit.client.CallPage
import com.github.servb.collabEdit.client.LoginPage
import com.github.servb.collabEdit.client.NotConnectedPage
import com.github.servb.collabEdit.client.ui.page.callPage
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
