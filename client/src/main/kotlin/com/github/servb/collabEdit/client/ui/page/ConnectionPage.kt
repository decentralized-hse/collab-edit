package com.github.servb.collabEdit.client.ui.page

import com.github.servb.collabEdit.client.ConnectionPage
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.h2
import react.dom.input
import react.dom.p
import react.dom.strong
import styled.css
import styled.styledButton
import styled.styledDiv

external interface ConnectionPageProps : RProps {

    var appState: ConnectionPage
}

val connectionPage = functionalComponent<ConnectionPageProps> { props ->
    var userNameToCall by useState("")

    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
            height = 100.pct
        }

        h2 {
            +"collab-edit"
        }

        p {
            +"OK "
            strong { +props.appState.userName }
            +", who do you want to connect with?"
        }

        styledDiv {
            css {
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
            }

            input(type = InputType.text) {
                attrs {
                    id = "connectToUsernameInput"
                    placeholder = "username to connect"
                    onInputFunction = {
                        userNameToCall = (it.target as HTMLInputElement).value
                    }
                    value = userNameToCall
                }
            }

            styledButton {
                css {
                    classes.addAll(listOf("btn-success", "btn"))
                    marginLeft = 10.px
                }

                attrs {
                    id = "connectBtn"
                    onClickFunction = {
                        props.appState.onConnect(userNameToCall)
                    }
                }

                +"Connect"
            }
        }
    }
}

fun RBuilder.connectionPage(handler: ConnectionPageProps.() -> Unit): ReactElement {
    return child(connectionPage) {
        attrs.handler()
    }
}
