package com.github.servb.collabEdit.client.ui

import com.github.servb.collabEdit.client.LoginPage
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*

external interface LoginPageProps : RProps {

    var appState: LoginPage
}

val loginPage = functionalComponent<LoginPageProps> { props ->
    val (userName, setUserName) = useState("")

    div("container text-center") {
        attrs {
            id = "loginPage"
        }

        div("row") {
            div("col-md-4 col-md-offset-4") {
                h2 {
                    +"WebRTC Text Demo. Please sign in"
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
                            setUserName((it.target as HTMLInputElement).value)
                        }
                        value = userName
                    }
                }
                button(classes = "btn btn-lg btn-primary btnblock") {
                    attrs {
                        id = "loginBtn"
                        onClickFunction = {
                            props.appState.onLogin(userName)
                        }
                    }

                    +"Sign In"
                }
            }
        }
    }
}

fun RBuilder.loginPage(handler: LoginPageProps.() -> Unit): ReactElement {
    return child(loginPage) {
        attrs.handler()
    }
}
