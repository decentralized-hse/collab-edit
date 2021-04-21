package com.github.servb.collabEdit.chronofold

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ChronofoldTest {

    @Test
    fun testEmptyString() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.End),
        )

        chronofold.getString() shouldBe ""
    }

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
    fun testFigure4a6b7String() {
        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Index(6)),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.End),
            Node(Value.Tombstone, Next.Index(2)),
        )

        chronofold.getString() shouldBe "INSK"
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

    @Test
    fun testFigure4a6Tob8Transition() {
        val ct = CausalTree(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
        )

        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Increment1),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.End),
        )

        val b6 = Operation(Timestamp("b", 6), Timestamp("a", 1), Value.Tombstone)

        ct.add(b6)
        chronofold.add(b6, ct)

        chronofold.getString() shouldBe "INSK"

        val b7 = Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M'))

        ct.add(b7)
        chronofold.add(b7, ct)

        chronofold.getString() shouldBe "MINSK"
    }

    @Test
    fun testFigure4a6b8Toc14Transition() {
        val ct = CausalTree(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
            Operation(Timestamp("b", 6), Timestamp("a", 1), Value.Tombstone),
            Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M')),
        )

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

        val c6 = Operation(Timestamp("c", 6), Timestamp("a", 5), Value.Tombstone)

        ct.add(c6)
        chronofold.add(c6, ct)

        chronofold.getString() shouldBe "MINS"

        val c7 = Operation(Timestamp("c", 7), Timestamp("c", 6), Value.Tombstone)

        ct.add(c7)
        chronofold.add(c7, ct)

        chronofold.getString() shouldBe "MIN"

        val c8 = Operation(Timestamp("c", 8), Timestamp("c", 7), Value.Tombstone)

        ct.add(c8)
        chronofold.add(c8, ct)

        chronofold.getString() shouldBe "MI"

        val c9 = Operation(Timestamp("c", 9), Timestamp("c", 8), Value.Tombstone)

        ct.add(c9)
        chronofold.add(c9, ct)

        chronofold.getString() shouldBe "M"

        val c10 = Operation(Timestamp("c", 10), Timestamp("c", 9), Value.Symbol('i'))

        ct.add(c10)
        chronofold.add(c10, ct)

        chronofold.getString() shouldBe "Mi"

        val c11 = Operation(Timestamp("c", 11), Timestamp("c", 10), Value.Symbol('n'))

        ct.add(c11)
        chronofold.add(c11, ct)

        chronofold.getString() shouldBe "Min"

        val c12 = Operation(Timestamp("c", 12), Timestamp("c", 11), Value.Symbol('s'))

        ct.add(c12)
        chronofold.add(c12, ct)

        chronofold.getString() shouldBe "Mins"

        val c13 = Operation(Timestamp("c", 13), Timestamp("c", 12), Value.Symbol('k'))

        ct.add(c13)
        chronofold.add(c13, ct)

        chronofold.getString() shouldBe "Minsk"
    }

    @Test
    fun testEmptyTimestamps() {
        val ct = CausalTree(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
        )

        val chronofold = Chronofold(
            Node(Value.Root, Next.End),
        )

        chronofold.getTimestamps(ct) shouldBe emptyList()
    }

    @Test
    fun testFigure4a6Timestamps() {
        val ct = CausalTree(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
        )

        val chronofold = Chronofold(
            Node(Value.Root, Next.Increment1),
            Node(Value.Symbol('P'), Next.Increment1),
            Node(Value.Symbol('I'), Next.Increment1),
            Node(Value.Symbol('N'), Next.Increment1),
            Node(Value.Symbol('S'), Next.Increment1),
            Node(Value.Symbol('K'), Next.End),
        )

        chronofold.getTimestamps(ct) shouldBe listOf(
            Timestamp("a", 1),
            Timestamp("a", 2),
            Timestamp("a", 3),
            Timestamp("a", 4),
            Timestamp("a", 5),
        )
    }

    @Test
    fun testFigure4a6b8Timestamps() {
        val ct = CausalTree(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
            Operation(Timestamp("b", 6), Timestamp("a", 1), Value.Tombstone),
            Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M')),
        )

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

        chronofold.getTimestamps(ct) shouldBe listOf(
            Timestamp("b", 7),
            Timestamp("a", 2),
            Timestamp("a", 3),
            Timestamp("a", 4),
            Timestamp("a", 5),
        )
    }

    @Test
    fun testFigure4a6b8c14imestamps() {
        val ct = CausalTree(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
            Operation(Timestamp("b", 6), Timestamp("a", 1), Value.Tombstone),
            Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M')),
            Operation(Timestamp("c", 6), Timestamp("a", 5), Value.Tombstone),
            Operation(Timestamp("c", 7), Timestamp("c", 6), Value.Tombstone),
            Operation(Timestamp("c", 8), Timestamp("c", 7), Value.Tombstone),
            Operation(Timestamp("c", 9), Timestamp("c", 8), Value.Tombstone),
            Operation(Timestamp("c", 10), Timestamp("c", 9), Value.Symbol('i')),
            Operation(Timestamp("c", 11), Timestamp("c", 10), Value.Symbol('n')),
            Operation(Timestamp("c", 12), Timestamp("c", 11), Value.Symbol('s')),
            Operation(Timestamp("c", 13), Timestamp("c", 12), Value.Symbol('k')),
        )

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

        chronofold.getTimestamps(ct) shouldBe listOf(
            Timestamp("b", 7),
            Timestamp("c", 10),
            Timestamp("c", 11),
            Timestamp("c", 12),
            Timestamp("c", 13),
        )
    }
}