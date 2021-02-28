package com.github.servb.collabEdit.client.ui.page

import com.github.servb.collabEdit.client.CallPage
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.h2
import react.dom.input
import react.dom.value
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledTextArea

external interface CallPageProps : RProps {

    var appState: CallPage
}

val callPage = functionalComponent<CallPageProps> { props ->
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

        styledDiv {
            css {
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
            }

            input(type = InputType.text) {
                attrs {
                    id = "callToUsernameInput"
                    placeholder = "username to call"
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
                    id = "callBtn"
                    onClickFunction = {
                        props.appState.onCall(userNameToCall)
                    }
                }

                +"Connect"
            }
            styledButton {
                css {
                    classes.addAll(listOf("btn-danger", "btn"))
                    marginLeft = 10.px
                }

                attrs {
                    id = "hangUpBtn"
                    onClickFunction = {
                        props.appState.onLeave()
                    }
                }

                +"Disconnect"
            }
        }

        styledTextArea {
            css {
                width = 100.pct
                height = 100.pct
                resize = Resize.none
                marginTop = 10.px
            }

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

fun RBuilder.callPage(handler: CallPageProps.() -> Unit): ReactElement {
    return child(callPage) {
        attrs.handler()
    }
}
