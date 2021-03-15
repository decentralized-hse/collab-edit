package com.github.servb.collabEdit.client

private const val TEXT_CURSOR = "|"

private fun createCursor(name: String): String = "$TEXT_CURSOR$name$TEXT_CURSOR"

// remove after https://youtrack.jetbrains.com/issue/KT-8113 is implemented
private fun String.indexOfOrNull(substring: String): Int? = this.indexOf(substring).takeIf { it >= 0 }

/**
 * Sent text representation:
 *
 *     "text|myName|texttext|otherName|"
 *
 * "|myName|" and "|otherName|" are cursor positions
 */
class SentTextRepresentation(val sentText: String) {

    fun toShown(myName: String, otherName: String): ShownTextRepresentation {
        val myNameCursor = createCursor(myName)
        val otherNameCursor = createCursor(otherName)
        val textWithCursor = sentText.replace(otherNameCursor, TEXT_CURSOR)
        val myCursorPosition = textWithCursor.indexOfOrNull(myNameCursor)
        val shownText = textWithCursor.replace(myNameCursor, "")

        return ShownTextRepresentation(myCursorPosition, shownText)
    }
}

/**
 * Internal and shown text representation:
 *
 *     "texttexttext|"
 *
 * my cursor isn't marked, other cursor is marked as "|"
 *
 * when more than two peers become available, need to split this class to two: "internal" and "shown" representations
 */
class ShownTextRepresentation(val myCursorPosition: Int?, val shownText: String) {

    fun toSent(myName: String, otherName: String): SentTextRepresentation {
        val myNameCursor = createCursor(myName)
        val otherNameCursor = createCursor(otherName)

        fun String.substituteOtherCursor(): String = this.replace(TEXT_CURSOR, otherNameCursor)

        val sentText = when (myCursorPosition) {
            null -> shownText.substituteOtherCursor()
            else -> shownText.substring(0 until myCursorPosition).substituteOtherCursor() +
                    myNameCursor +
                    shownText.substring(myCursorPosition).substituteOtherCursor()
        }

        return SentTextRepresentation(sentText)
    }
}
