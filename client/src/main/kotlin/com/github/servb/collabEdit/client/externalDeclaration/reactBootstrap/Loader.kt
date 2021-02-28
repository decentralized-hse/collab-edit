@file:JsModule("react-bootstrap")
@file:JsNonModule

package com.github.servb.collabEdit.client.externalDeclaration.reactBootstrap

import react.RClass
import react.RProps

// https://react-bootstrap.github.io/components/spinners/

external interface SpinnerProps : RProps {

    /** 'border' | 'grow'. */
    var animation: String

    /** Element type, for example, 'div' or 'span'. */
    var `as`: String

    /** 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark'. */
    var variant: String
}

@JsName("Spinner")
external val spinner: RClass<SpinnerProps>
