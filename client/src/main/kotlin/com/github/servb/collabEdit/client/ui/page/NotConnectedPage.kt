package com.github.servb.collabEdit.client.ui.page

import com.github.servb.collabEdit.client.ui.element.loadingIndicator
import kotlinx.css.*
import react.*
import styled.css
import styled.styledDiv
import styled.styledSpan

val notConnectedPage = functionalComponent<RProps> {
    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.center
            height = 100.pct
        }

        +"Connecting to the signaling server..."

        styledSpan {
            css {
                padding = "5px"
            }

            loadingIndicator()
        }
    }
}

fun RBuilder.notConnectedPage(): ReactElement {
    return child(notConnectedPage)
}
