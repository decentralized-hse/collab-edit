package com.github.servb.collabEdit.chronofold

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ChronofoldTest {

    @Test
    fun testFigure4a6String() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Increment1),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.End),
        )

        chronofold.getString() shouldBe "PINSK"
    }

    @Test
    fun testFigure4a6b8String() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Index(6)),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.End),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Symbol('M'), Next.Index(2)),
        )

        chronofold.getString() shouldBe "MINSK"
    }

    @Test
    fun testFigure4a6c14String() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Increment1),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Symbol('i'), Next.Increment1),
            Node(Value.Symbol('n'), Next.Increment1),
            Node(Value.Symbol('s'), Next.Increment1),
            Node(Value.Symbol('k'), Next.End),
        )

        chronofold.getString() shouldBe "Pinsk"
    }

    @Test
    fun testFigure4a6c14b8String() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Index(14)),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Symbol('i'), Next.Increment1),
            Node(Value.Symbol('n'), Next.Increment1),
            Node(Value.Symbol('s'), Next.Increment1),
            Node(Value.Symbol('k'), Next.End),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Symbol('M'), Next.Index(2)),
        )

        chronofold.getString() shouldBe "Minsk"
    }

    @Test
    fun testFigure4a6b8c14String() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Index(6)),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.Index(8)),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Symbol('M'), Next.Index(2)),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Tombstone, Next.Increment1),
            Node(Value.Symbol('i'), Next.Increment1),
            Node(Value.Symbol('n'), Next.Increment1),
            Node(Value.Symbol('s'), Next.Increment1),
            Node(Value.Symbol('k'), Next.End),
        )

        chronofold.getString() shouldBe "Minsk"
    }
}