package com.github.servb.collabEdit.client.ui.element

import com.github.servb.collabEdit.client.externalDeclaration.reactBootstrap.spinner
import react.*
import react.dom.span

val loadingIndicator = functionalComponent<RProps> {
    spinner {
        attrs {
            animation = "border"
            `as` = "span"
            variant = "primary"
        }

        span("sr-only") {
            +"Loading..."
        }
    }
}

fun RBuilder.loadingIndicator(): ReactElement {
    return child(loadingIndicator)
}
