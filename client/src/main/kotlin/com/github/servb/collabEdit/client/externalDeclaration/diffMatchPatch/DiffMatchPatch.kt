@file:JsModule("diff-match-patch")
@file:JsNonModule

package com.github.servb.collabEdit.client.externalDeclaration.diffMatchPatch

external class Diffs
external class Patches

@JsName("diff_match_patch")
external class DiffMatchPatch {

    fun diff_main(text1: String, text2: String): Diffs
    fun patch_make(text1: String, diffs: Diffs): Patches
    fun patch_toText(patches: Patches): String
    fun patch_fromText(patches: String): Patches
    fun patch_apply(patches: Patches, text1: String): Array<Any>
}
