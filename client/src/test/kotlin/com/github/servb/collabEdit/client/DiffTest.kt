package com.github.servb.collabEdit.client

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

        ops.forEach {
            ct.add(it)
            chronofold.add(it, ct)
        }

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

        ops.forEach {
            ct.add(it)
            chronofold.add(it, ct)
        }

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

        ops.forEach {
            ct.add(it)
            chronofold.add(it, ct)
        }

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

        ops.forEach {
            ct.add(it)
            chronofold.add(it, ct)
        }

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

        ops.forEach {
            ct.add(it)
            chronofold.add(it, ct)
        }

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

        ops.forEach {
            ct.add(it)
            chronofold.add(it, ct)
        }

        println(chronofold)
        println(ct)

        chronofold.getString() shouldBe text2
    }
}