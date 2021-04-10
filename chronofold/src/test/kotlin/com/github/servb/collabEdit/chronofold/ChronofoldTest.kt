package com.github.servb.collabEdit.chronofold

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ChronofoldTest {

    @Test
    fun testFigure4a6() {
        val chronofold = Chronofold(
            Node(Value.Root, Timestamp("a", 1), Next.Increment1),
            Node(Value.Symbol('P'), Timestamp("a", 2), Next.Increment1),
            Node(Value.Symbol('I'), Timestamp("a", 3), Next.Increment1),
            Node(Value.Symbol('N'), Timestamp("a", 4), Next.Increment1),
            Node(Value.Symbol('S'), Timestamp("a", 5), Next.Increment1),
            Node(Value.Symbol('K'), Timestamp("a", 6), Next.End),
        )

        chronofold.getString() shouldBe "PINSK"
    }

    @Test
    fun testFigure4a6b8() {
        val chronofold = Chronofold(
            Node(Value.Root, Timestamp("a", 1), Next.Increment1),
            Node(Value.Symbol('P'), Timestamp("a", 2), Next.Index(Timestamp("b", 7))),
            Node(Value.Symbol('I'), Timestamp("a", 3), Next.Increment1),
            Node(Value.Symbol('N'), Timestamp("a", 4), Next.Increment1),
            Node(Value.Symbol('S'), Timestamp("a", 5), Next.Increment1),
            Node(Value.Symbol('K'), Timestamp("a", 6), Next.End),
            Node(Value.Tombstone, Timestamp("b", 7), Next.Increment1),
            Node(Value.Symbol('M'), Timestamp("b", 8), Next.Index(Timestamp("a", 3))),
        )

        chronofold.getString() shouldBe "MINSK"
    }

    @Test
    fun testFigure4a6c14() {
        val chronofold = Chronofold(
            Node(Value.Root, Timestamp("a", 1), Next.Increment1),
            Node(Value.Symbol('P'), Timestamp("a", 2), Next.Increment1),
            Node(Value.Symbol('I'), Timestamp("a", 3), Next.Increment1),
            Node(Value.Symbol('N'), Timestamp("a", 4), Next.Increment1),
            Node(Value.Symbol('S'), Timestamp("a", 5), Next.Increment1),
            Node(Value.Symbol('K'), Timestamp("a", 6), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 7), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 8), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 9), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 10), Next.Increment1),
            Node(Value.Symbol('i'), Timestamp("c", 11), Next.Increment1),
            Node(Value.Symbol('n'), Timestamp("c", 12), Next.Increment1),
            Node(Value.Symbol('s'), Timestamp("c", 13), Next.Increment1),
            Node(Value.Symbol('k'), Timestamp("c", 14), Next.End),
        )

        chronofold.getString() shouldBe "Pinsk"
    }

    @Test
    fun testFigure4a6c14b8() {
        val chronofold = Chronofold(
            Node(Value.Root, Timestamp("a", 1), Next.Increment1),
            Node(Value.Symbol('P'), Timestamp("a", 2), Next.Index(Timestamp("b", 7))),
            Node(Value.Symbol('I'), Timestamp("a", 3), Next.Increment1),
            Node(Value.Symbol('N'), Timestamp("a", 4), Next.Increment1),
            Node(Value.Symbol('S'), Timestamp("a", 5), Next.Increment1),
            Node(Value.Symbol('K'), Timestamp("a", 6), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 7), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 8), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 9), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 10), Next.Increment1),
            Node(Value.Symbol('i'), Timestamp("c", 11), Next.Increment1),
            Node(Value.Symbol('n'), Timestamp("c", 12), Next.Increment1),
            Node(Value.Symbol('s'), Timestamp("c", 13), Next.Increment1),
            Node(Value.Symbol('k'), Timestamp("c", 14), Next.End),
            Node(Value.Tombstone, Timestamp("b", 7), Next.Increment1),
            Node(Value.Symbol('M'), Timestamp("b", 8), Next.Index(Timestamp("a", 3))),
        )

        chronofold.getString() shouldBe "Minsk"
    }

    @Test
    fun testFigure4a6b8c14() {
        val chronofold = Chronofold(
            Node(Value.Root, Timestamp("a", 1), Next.Increment1),
            Node(Value.Symbol('P'), Timestamp("a", 2), Next.Index(Timestamp("b", 7))),
            Node(Value.Symbol('I'), Timestamp("a", 3), Next.Increment1),
            Node(Value.Symbol('N'), Timestamp("a", 4), Next.Increment1),
            Node(Value.Symbol('S'), Timestamp("a", 5), Next.Increment1),
            Node(Value.Symbol('K'), Timestamp("a", 6), Next.Index(Timestamp("c", 7))),
            Node(Value.Tombstone, Timestamp("b", 7), Next.Increment1),
            Node(Value.Symbol('M'), Timestamp("b", 8), Next.Index(Timestamp("a", 3))),
            Node(Value.Tombstone, Timestamp("c", 7), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 8), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 9), Next.Increment1),
            Node(Value.Tombstone, Timestamp("c", 10), Next.Increment1),
            Node(Value.Symbol('i'), Timestamp("c", 11), Next.Increment1),
            Node(Value.Symbol('n'), Timestamp("c", 12), Next.Increment1),
            Node(Value.Symbol('s'), Timestamp("c", 13), Next.Increment1),
            Node(Value.Symbol('k'), Timestamp("c", 14), Next.End),
        )

        chronofold.getString() shouldBe "Minsk"
    }
}