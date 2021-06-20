package com.github.servb.collabEdit.client.ui.page

import com.github.servb.collabEdit.client.LoginPage
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.h2
import react.dom.input
import react.dom.label
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledInput

external interface LoginPageProps : RProps {

    var appState: LoginPage
}

val loginPage = functionalComponent<LoginPageProps> { props ->
    var userName by useState("")
    var useWebRtc by useState(false)

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

        styledDiv {
            css {
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.spaceAround
            }

            label("sr-only") {
                attrs {
                    this["htmlFor"] = "usernameInput"  // https://github.com/JetBrains/kotlin-wrappers/issues/365
                }
                +"Login"
            }
            input(classes = "form-control formgroup", type = InputType.email) {
                attrs {
                    autoFocus = true
                    id = "usernameInput"
                    placeholder = "Login"
                    required = true
                    onInputFunction = {
                        userName = (it.target as HTMLInputElement).value
                    }
                    value = userName
                }
            }
            styledButton {
                css {
                    classes.addAll(listOf("btn", "btn-lg", "btn-primary", "btnblock"))
                    width = 200.px
                    marginLeft = 10.px
                }

                attrs {
                    id = "loginBtn"
                    onClickFunction = {
                        props.appState.onLogin(userName, useWebRtc)
                    }
                }

                +"Sign In"
            }
        }

        styledDiv {
            css {
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.spaceAround
            }

            label {
                styledInput(InputType.checkBox) {
                    css {
                        marginRight = 5.px
                    }
                    attrs {
                        onClickFunction = {
                            useWebRtc = !useWebRtc
                        }
                        value = useWebRtc.toString()
                        id = "useWebrtc"
                    }
                }
                +"Connect using p2p WebRTC (may require white IP)"
            }
        }
    }
}

fun RBuilder.loginPage(handler: LoginPageProps.() -> Unit): ReactElement {
    return child(loginPage) {
        attrs.handler()
    }
}
