package com.github.servb.collabEdit.chronofold

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class UtilTest {

    @Test
    fun testCalculateNext() {
        calculateNext(10, Next.Increment1, 100500) shouldBe Next.Index(11)
        calculateNext(10, Next.End, 100500) shouldBe Next.End
        calculateNext(10, Next.Index(15), 100500) shouldBe Next.Index(15)
        calculateNext(10, Next.Index(100500 + 1), 100500) shouldBe Next.Increment1
    }

    @Test
    fun testNormalizeNext() {
        normalizeNext(100, 101) shouldBe Next.Increment1
        normalizeNext(1000, 500) shouldBe Next.Index(500)
    }

    @Test
    fun testIncrementOrEnd() {
        incrementOrEnd(0, 0) shouldBe Next.End
        incrementOrEnd(0, 1) shouldBe Next.Increment1
        incrementOrEnd(1, 1) shouldBe Next.End
    }

    @Test
    fun testCreateInitialDataEmpty() {
        val (ct, chronofold) = createInitialData("", "init")

        ct shouldBe CausalTree(
            Operation(Timestamp("init", 0), Timestamp("init", 0), Value.Root),
        )
        chronofold shouldBe Chronofold(
            Node(Value.Root, Next.End),
        )
    }

    @Test
    fun testCreateInitialDataStr() {
        val (ct, chronofold) = createInitialData("abc", "start")

        ct shouldBe CausalTree(
            Operation(Timestamp("start", 0), Timestamp("start", 0), Value.Root),
            Operation(Timestamp("start", 1), Timestamp("start", 0), Value.Symbol('a')),
            Operation(Timestamp("start", 2), Timestamp("start", 1), Value.Symbol('b')),
            Operation(Timestamp("start", 3), Timestamp("start", 2), Value.Symbol('c')),
        )
        chronofold shouldBe Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('a'), Next.Increment1),
            Node(Value.Symbol('b'), Next.Increment1),
            Node(Value.Symbol('c'), Next.End),
        )
    }
}