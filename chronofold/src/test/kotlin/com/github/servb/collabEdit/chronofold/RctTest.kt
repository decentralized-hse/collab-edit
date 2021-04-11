package com.github.servb.collabEdit.chronofold

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class RctTest {

    @Test
    fun testFigure4a6Ndx() {
        val rct = ReplicatedCausalTreeForSingleProcess(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
        )

        rct.ndx(Timestamp("a", 0)) shouldBe 0
        rct.ndx(Timestamp("a", 1)) shouldBe 1
        rct.ndx(Timestamp("a", 2)) shouldBe 2
        rct.ndx(Timestamp("a", 3)) shouldBe 3
        rct.ndx(Timestamp("a", 4)) shouldBe 4
        rct.ndx(Timestamp("a", 5)) shouldBe 5
    }

    @Test
    fun testFigure4a6b8Ndx() {
        val rct = ReplicatedCausalTreeForSingleProcess(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
            Operation(Timestamp("b", 6), Timestamp("a", 1), Value.Tombstone),
            Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M')),
        )

        rct.ndx(Timestamp("a", 0)) shouldBe 0
        rct.ndx(Timestamp("a", 1)) shouldBe 1
        rct.ndx(Timestamp("a", 2)) shouldBe 2
        rct.ndx(Timestamp("a", 3)) shouldBe 3
        rct.ndx(Timestamp("a", 4)) shouldBe 4
        rct.ndx(Timestamp("a", 5)) shouldBe 5
        rct.ndx(Timestamp("b", 6)) shouldBe 6
        rct.ndx(Timestamp("b", 7)) shouldBe 7
    }

    @Test
    fun testFigure4a6c14Ndx() {
        val rct = ReplicatedCausalTreeForSingleProcess(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
            Operation(Timestamp("c", 6), Timestamp("a", 5), Value.Tombstone),
            Operation(Timestamp("c", 7), Timestamp("c", 6), Value.Tombstone),
            Operation(Timestamp("c", 8), Timestamp("c", 7), Value.Tombstone),
            Operation(Timestamp("c", 9), Timestamp("c", 8), Value.Tombstone),
            Operation(Timestamp("c", 10), Timestamp("c", 9), Value.Symbol('i')),
            Operation(Timestamp("c", 11), Timestamp("c", 10), Value.Symbol('n')),
            Operation(Timestamp("c", 12), Timestamp("c", 11), Value.Symbol('s')),
            Operation(Timestamp("c", 13), Timestamp("c", 12), Value.Symbol('k')),
        )

        rct.ndx(Timestamp("a", 0)) shouldBe 0
        rct.ndx(Timestamp("a", 1)) shouldBe 1
        rct.ndx(Timestamp("a", 2)) shouldBe 2
        rct.ndx(Timestamp("a", 3)) shouldBe 3
        rct.ndx(Timestamp("a", 4)) shouldBe 4
        rct.ndx(Timestamp("a", 5)) shouldBe 5
        rct.ndx(Timestamp("c", 6)) shouldBe 6
        rct.ndx(Timestamp("c", 7)) shouldBe 7
        rct.ndx(Timestamp("c", 8)) shouldBe 8
        rct.ndx(Timestamp("c", 9)) shouldBe 9
        rct.ndx(Timestamp("c", 10)) shouldBe 10
        rct.ndx(Timestamp("c", 11)) shouldBe 11
        rct.ndx(Timestamp("c", 12)) shouldBe 12
        rct.ndx(Timestamp("c", 13)) shouldBe 13
    }

    @Test
    fun testFigure4a6c14b8Ndx() {
        val rct = ReplicatedCausalTreeForSingleProcess(
            Operation(Timestamp("a", 0), Timestamp("a", 0), Value.Root),
            Operation(Timestamp("a", 1), Timestamp("a", 0), Value.Symbol('P')),
            Operation(Timestamp("a", 2), Timestamp("a", 1), Value.Symbol('I')),
            Operation(Timestamp("a", 3), Timestamp("a", 2), Value.Symbol('N')),
            Operation(Timestamp("a", 4), Timestamp("a", 3), Value.Symbol('S')),
            Operation(Timestamp("a", 5), Timestamp("a", 4), Value.Symbol('K')),
            Operation(Timestamp("c", 6), Timestamp("a", 5), Value.Tombstone),
            Operation(Timestamp("c", 7), Timestamp("c", 6), Value.Tombstone),
            Operation(Timestamp("c", 8), Timestamp("c", 7), Value.Tombstone),
            Operation(Timestamp("c", 9), Timestamp("c", 8), Value.Tombstone),
            Operation(Timestamp("c", 10), Timestamp("c", 9), Value.Symbol('i')),
            Operation(Timestamp("c", 11), Timestamp("c", 10), Value.Symbol('n')),
            Operation(Timestamp("c", 12), Timestamp("c", 11), Value.Symbol('s')),
            Operation(Timestamp("c", 13), Timestamp("c", 12), Value.Symbol('k')),
            Operation(Timestamp("b", 6), Timestamp("a", 1), Value.Tombstone),
            Operation(Timestamp("b", 7), Timestamp("b", 6), Value.Symbol('M')),
        )

        rct.ndx(Timestamp("a", 0)) shouldBe 0
        rct.ndx(Timestamp("a", 1)) shouldBe 1
        rct.ndx(Timestamp("a", 2)) shouldBe 2
        rct.ndx(Timestamp("a", 3)) shouldBe 3
        rct.ndx(Timestamp("a", 4)) shouldBe 4
        rct.ndx(Timestamp("a", 5)) shouldBe 5
        rct.ndx(Timestamp("c", 6)) shouldBe 6
        rct.ndx(Timestamp("c", 7)) shouldBe 7
        rct.ndx(Timestamp("c", 8)) shouldBe 8
        rct.ndx(Timestamp("c", 9)) shouldBe 9
        rct.ndx(Timestamp("c", 10)) shouldBe 10
        rct.ndx(Timestamp("c", 11)) shouldBe 11
        rct.ndx(Timestamp("c", 12)) shouldBe 12
        rct.ndx(Timestamp("c", 13)) shouldBe 13
        rct.ndx(Timestamp("b", 6)) shouldBe 14
        rct.ndx(Timestamp("b", 7)) shouldBe 15
    }

    @Test
    fun testFigure4a6b8c14Ndx() {
        val rct = ReplicatedCausalTreeForSingleProcess(
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

        rct.ndx(Timestamp("a", 0)) shouldBe 0
        rct.ndx(Timestamp("a", 1)) shouldBe 1
        rct.ndx(Timestamp("a", 2)) shouldBe 2
        rct.ndx(Timestamp("a", 3)) shouldBe 3
        rct.ndx(Timestamp("a", 4)) shouldBe 4
        rct.ndx(Timestamp("a", 5)) shouldBe 5
        rct.ndx(Timestamp("b", 6)) shouldBe 6
        rct.ndx(Timestamp("b", 7)) shouldBe 7
        rct.ndx(Timestamp("c", 6)) shouldBe 8
        rct.ndx(Timestamp("c", 7)) shouldBe 9
        rct.ndx(Timestamp("c", 8)) shouldBe 10
        rct.ndx(Timestamp("c", 9)) shouldBe 11
        rct.ndx(Timestamp("c", 10)) shouldBe 12
        rct.ndx(Timestamp("c", 11)) shouldBe 13
        rct.ndx(Timestamp("c", 12)) shouldBe 14
        rct.ndx(Timestamp("c", 13)) shouldBe 15
    }
}
