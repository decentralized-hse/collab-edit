package com.github.servb.collabEdit.client.wrappers.diffMatchPatch

fun Array<Any>.toTextAndResults(): Pair<String, Array<Boolean>> =
    this[0].unsafeCast<String>() to this[1].unsafeCast<Array<Boolean>>()
