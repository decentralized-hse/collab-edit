package com.github.servb.collabEdit.client

import com.github.servb.collabEdit.chronofold.applyTo
import com.github.servb.collabEdit.chronofold.createInitialData
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DiffTest {

    private fun diffTest(text1: String, text2: String, expectedOpsSize: Int? = null, authorName: String = "myName") {
        fun String.expandCaret() = replace("|", "|$authorName|")

        val initialText = text1.expandCaret()
        val expectedText = text2.expandCaret()
        val (ct, chronofold) = createInitialData(initialText, authorName)

        val ops = diff(expectedText, chronofold, ct, authorName)

        expectedOpsSize?.let { ops shouldHaveSize it }

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe expectedText
    }

    @Test
    fun testInsertOneSymbol() = diffTest(
        text1 = "abc|123",
        text2 = "abci|123",
        expectedOpsSize = 1,
    )

    @Test
    fun testInsertOneSymbolToStart() = diffTest(
        text1 = "abc|123",
        text2 = "iabc|123",
        expectedOpsSize = 1,
    )

    @Test
    fun testInsertOneSymbolToEmpty() = diffTest(
        text1 = "",
        text2 = "_",
        expectedOpsSize = 1,
    )

    @Test
    fun testRemoveOneSymbol() = diffTest(
        text1 = "abc|123",
        text2 = "ab|123",
        expectedOpsSize = 1,
    )

    @Test
    fun testInsertMultipleSymbols() = diffTest(
        text1 = "abc|123",
        text2 = "abcABC|123",
        expectedOpsSize = 3,
    )

    @Test
    fun testInsertMultipleSymbolsToStart() = diffTest(
        text1 = "|",
        text2 = "my message, 12345 ms|",
    )

    @Test
    fun testRemoveMultipleSymbols() = diffTest(
        text1 = "abc|123",
        text2 = "|123",
        expectedOpsSize = 3,
    )

    @Test
    fun testInsertMultipleSymbolsToStartAndEnd() = diffTest(
        text1 = "|",
        text2 = "my message, 12345 ms|abcd",
    )

    @Test
    fun testCursorMoveLeft1() = diffTest(
        text1 = "12|3",
        text2 = "1|23",
    )

    @Test
    fun testCursorMoveLeft2() = diffTest(
        text1 = "1122|33",
        text2 = "11|2233",
    )

    @Test
    fun testCursorMoveLeft3() = diffTest(
        text1 = "1122|33",
        text2 = "|112233",
    )

    @Test
    fun testCursorMoveRight1() = diffTest(
        text1 = "1|23",
        text2 = "12|3",
    )

    @Test
    fun testCursorMoveRight2() = diffTest(
        text1 = "11|2233",
        text2 = "1122|33",
    )

    @Test
    fun testCursorMoveRight3() = diffTest(
        text1 = "11|2233",
        text2 = "112233|",
    )

    @Test
    fun testReplace() = diffTest(
        text1 = "11aaa|2233",
        text2 = "11bbb|2233",
    )
}