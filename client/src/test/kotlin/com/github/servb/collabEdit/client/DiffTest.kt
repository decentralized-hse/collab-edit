package com.github.servb.collabEdit.client

import com.github.servb.collabEdit.chronofold.applyTo
import com.github.servb.collabEdit.chronofold.createInitialData
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DiffTest {

    @Test
    fun testInsertOneSymbol() {
        val authorName = "myName"
        val text1 = "abc|$authorName|123"
        val text2 = "abci|$authorName|123"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops shouldHaveSize 1

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testInsertOneSymbolToStart() {
        val authorName = "myName"
        val text1 = "abc|$authorName|123"
        val text2 = "iabc|$authorName|123"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops shouldHaveSize 1

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testInsertOneSymbolToEmpty() {
        val authorName = "myName"
        val text1 = ""
        val text2 = "_"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops shouldHaveSize 1

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testRemoveOneSymbol() {
        val authorName = "myName"
        val text1 = "abc|$authorName|123"
        val text2 = "ab|$authorName|123"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops shouldHaveSize 1

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testInsertMultipleSymbols() {
        val authorName = "myName"
        val text1 = "abc|$authorName|123"
        val text2 = "abcABC|$authorName|123"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops shouldHaveSize 3

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testInsertMultipleSymbolsToStart() {
        val authorName = "myName"
        val text1 = "|$authorName|"
        val text2 = "my message, 12345 ms|$authorName|"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testRemoveMultipleSymbols() {
        val authorName = "myName"
        val text1 = "abc|$authorName|123"
        val text2 = "|$authorName|123"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops shouldHaveSize 3

        ops.applyTo(ct, chronofold)

        println(chronofold)
        println(ct)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testInsertMultipleSymbolsToStartAndEnd() {
        val authorName = "myName"
        val text1 = "|$authorName|"
        val text2 = "my message, 12345 ms|$authorName|abcd"
        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testCursorMoveLeft1() {
        val authorName = "myName"
        val text1 = "12|$authorName|3"
        val text2 = "1|$authorName|23"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testCursorMoveLeft2() {
        val authorName = "myName"
        val text1 = "1122|$authorName|33"
        val text2 = "11|$authorName|2233"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testCursorMoveLeft3() {
        val authorName = "myName"
        val text1 = "1122|$authorName|33"
        val text2 = "|$authorName|112233"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testCursorMoveRight1() {
        val authorName = "myName"
        val text1 = "1|$authorName|23"
        val text2 = "12|$authorName|3"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testCursorMoveRight2() {
        val authorName = "myName"
        val text1 = "11|$authorName|2233"
        val text2 = "1122|$authorName|33"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testCursorMoveRight3() {
        val authorName = "myName"
        val text1 = "11|$authorName|2233"
        val text2 = "112233|$authorName|"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }

    @Test
    fun testReplace() {
        val authorName = "myName"
        val text1 = "11aaa|$authorName|2233"
        val text2 = "11bbb|$authorName|2233"

        val (ct, chronofold) = createInitialData(text1, authorName)

        val ops = diff(text2, chronofold, ct, authorName)

        ops.applyTo(ct, chronofold)

        chronofold.getString() shouldBe text2
    }
}