package com.github.servb.collabEdit.client.ui.page

import com.github.servb.collabEdit.client.CollaborationPage
import com.github.servb.collabEdit.client.ShownTextRepresentation
import kotlinx.css.*
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSelectFunction
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.div
import react.dom.h2
import react.dom.strong
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledTextArea

external interface CollaborationPageProps : RProps {

    var appState: CollaborationPage
}

val collaborationPage = functionalComponent<CollaborationPageProps> { props ->
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

            div {
                +"OK "
                strong { +props.appState.userName }
                +", connected to "
                strong { +props.appState.otherUserName }
                +". You can collaborate below."
            }

            styledButton {
                css {
                    classes.addAll(listOf("btn-danger", "btn"))
                    marginLeft = 10.px
                }

                attrs {
                    id = "disconnectBtn"
                    onClickFunction = {
                        props.appState.onDisconnect()
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

            fun updateText(element: HTMLTextAreaElement) {
                props.appState.onTextChange(ShownTextRepresentation(element.selectionStart, element.value))
            }

            attrs {
                id = "text"
                placeholder = "Collaborate here!"
                onSelectFunction = {
                    updateText(it.target as HTMLTextAreaElement)
                }
                onChangeFunction = {
                    updateText(it.target as HTMLTextAreaElement)
                }

                val textAreaRef = useRef<HTMLTextAreaElement?>(null)
                ref = textAreaRef

                useEffect {
                    textAreaRef.current?.value = props.appState.text.shownText
                    textAreaRef.current?.selectionStart = props.appState.text.myCursorPosition
                    textAreaRef.current?.selectionEnd = props.appState.text.myCursorPosition
                }
            }
        }
    }
}

fun RBuilder.collaborationPage(handler: CollaborationPageProps.() -> Unit): ReactElement {
    return child(collaborationPage) {
        attrs.handler()
    }
}
