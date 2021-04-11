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
    fun testFigure4aTo6b8Transition() {
        val rct = ReplicatedCausalTreeForSingleProcess(
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

        rct.add(b6)
        chronofold.add(b6, rct)

        chronofold.getString() shouldBe "INSK"

        val b7 = Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M'))

        rct.add(b7)
        chronofold.add(b7, rct)

        chronofold.getString() shouldBe "MINSK"
    }
}