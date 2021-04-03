package com.github.servb.collabEdit.chronofold

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ChronofoldTest {

    @Test
    fun testFigure4a6() {
        val chronofold = Chronofold(
            Node(Value.Root, "a", 1, Next.Increment1),
            Node(Value.Symbol('P'), "a", 2, Next.Increment1),
            Node(Value.Symbol('I'), "a", 3, Next.Increment1),
            Node(Value.Symbol('N'), "a", 4, Next.Increment1),
            Node(Value.Symbol('S'), "a", 5, Next.Increment1),
            Node(Value.Symbol('K'), "a", 6, Next.End),
        )

        chronofold.getString() shouldBe "PINSK"
    }

    @Test
    fun testFigure4a6b8() {
        val chronofold = Chronofold(
            Node(Value.Root, "a", 1, Next.Increment1),
            Node(Value.Symbol('P'), "a", 2, Next.Index("b", 7)),
            Node(Value.Symbol('I'), "a", 3, Next.Increment1),
            Node(Value.Symbol('N'), "a", 4, Next.Increment1),
            Node(Value.Symbol('S'), "a", 5, Next.Increment1),
            Node(Value.Symbol('K'), "a", 6, Next.End),
            Node(Value.Tombstone, "b", 7, Next.Increment1),
            Node(Value.Symbol('M'), "b", 8, Next.Index("a", 3)),
        )

        chronofold.getString() shouldBe "MINSK"
    }

    @Test
    fun testFigure4a6c14() {
        val chronofold = Chronofold(
            Node(Value.Root, "a", 1, Next.Increment1),
            Node(Value.Symbol('P'), "a", 2, Next.Increment1),
            Node(Value.Symbol('I'), "a", 3, Next.Increment1),
            Node(Value.Symbol('N'), "a", 4, Next.Increment1),
            Node(Value.Symbol('S'), "a", 5, Next.Increment1),
            Node(Value.Symbol('K'), "a", 6, Next.Increment1),
            Node(Value.Tombstone, "c", 7, Next.Increment1),
            Node(Value.Tombstone, "c", 8, Next.Increment1),
            Node(Value.Tombstone, "c", 9, Next.Increment1),
            Node(Value.Tombstone, "c", 10, Next.Increment1),
            Node(Value.Symbol('i'), "c", 11, Next.Increment1),
            Node(Value.Symbol('n'), "c", 12, Next.Increment1),
            Node(Value.Symbol('s'), "c", 13, Next.Increment1),
            Node(Value.Symbol('k'), "c", 14, Next.End),
        )

        chronofold.getString() shouldBe "Pinsk"
    }

    @Test
    fun testFigure4a6c14b8() {
        val chronofold = Chronofold(
            Node(Value.Root, "a", 1, Next.Increment1),
            Node(Value.Symbol('P'), "a", 2, Next.Index("b", 7)),
            Node(Value.Symbol('I'), "a", 3, Next.Increment1),
            Node(Value.Symbol('N'), "a", 4, Next.Increment1),
            Node(Value.Symbol('S'), "a", 5, Next.Increment1),
            Node(Value.Symbol('K'), "a", 6, Next.Increment1),
            Node(Value.Tombstone, "c", 7, Next.Increment1),
            Node(Value.Tombstone, "c", 8, Next.Increment1),
            Node(Value.Tombstone, "c", 9, Next.Increment1),
            Node(Value.Tombstone, "c", 10, Next.Increment1),
            Node(Value.Symbol('i'), "c", 11, Next.Increment1),
            Node(Value.Symbol('n'), "c", 12, Next.Increment1),
            Node(Value.Symbol('s'), "c", 13, Next.Increment1),
            Node(Value.Symbol('k'), "c", 14, Next.End),
            Node(Value.Tombstone, "b", 7, Next.Increment1),
            Node(Value.Symbol('M'), "b", 8, Next.Index("a", 3)),
        )

        chronofold.getString() shouldBe "Minsk"
    }

    @Test
    fun testFigure4a6b8c14() {
        val chronofold = Chronofold(
            Node(Value.Root, "a", 1, Next.Increment1),
            Node(Value.Symbol('P'), "a", 2, Next.Index("b", 7)),
            Node(Value.Symbol('I'), "a", 3, Next.Increment1),
            Node(Value.Symbol('N'), "a", 4, Next.Increment1),
            Node(Value.Symbol('S'), "a", 5, Next.Increment1),
            Node(Value.Symbol('K'), "a", 6, Next.Index("c", 7)),
            Node(Value.Tombstone, "b", 7, Next.Increment1),
            Node(Value.Symbol('M'), "b", 8, Next.Index("a", 3)),
            Node(Value.Tombstone, "c", 7, Next.Increment1),
            Node(Value.Tombstone, "c", 8, Next.Increment1),
            Node(Value.Tombstone, "c", 9, Next.Increment1),
            Node(Value.Tombstone, "c", 10, Next.Increment1),
            Node(Value.Symbol('i'), "c", 11, Next.Increment1),
            Node(Value.Symbol('n'), "c", 12, Next.Increment1),
            Node(Value.Symbol('s'), "c", 13, Next.Increment1),
            Node(Value.Symbol('k'), "c", 14, Next.End),
        )

        chronofold.getString() shouldBe "Minsk"
    }
}