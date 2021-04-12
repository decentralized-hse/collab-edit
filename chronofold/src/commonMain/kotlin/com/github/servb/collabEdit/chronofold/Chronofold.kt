package com.github.servb.collabEdit.chronofold

data class Node(
    val value: Value,
    var next: Next,
)

sealed class Value {

    /** ∅. */
    object Root : Value() {

        override fun toString(): String = "∅"
    }

    // todo: replace Char (uint16) with UInt (uint32)
    data class Symbol(val char: Char) : Value()

    /** ⌫. */
    object Tombstone : Value() {

        override fun toString(): String = "⌫"
    }
}

sealed class Next {

    object Increment1 : Next() {

        override fun toString(): String = "++"
    }

    data class Index(val idx: Int) : Next()

    object End : Next() {

        override fun toString(): String = "End"
    }
}

class Chronofold private constructor(private val log: MutableList<Node>) {

    constructor(vararg nodes: Node) : this(nodes.toMutableList())

    fun getString(): String {
        val result = StringBuilder()
        var logIdx = 0
        while (true) {
            val currentNode = log[logIdx]
            when (currentNode.value) {
                is Value.Root -> {
                }
                is Value.Symbol -> result.append(currentNode.value.char)
                is Value.Tombstone -> result.deleteAt(result.lastIndex)
            }
            when (val next = currentNode.next) {
                is Next.Increment1 -> ++logIdx
                is Next.Index -> logIdx = next.idx
                is Next.End -> break
            }
        }
        return result.toString()
    }

    fun add(operation: Operation, ct: CausalTree) {
        val ref = operation.ref
        val j = ct.ndx(ref)
        check(ref.authorIndex < operation.timestamp.authorIndex) { "k < i is broken" }
        check(ref.authorIndex <= j) { "k <= j is broken" }
        val prev = log[j]
        val addedIndex = log.size
        val next = calculateNext(j, prev.next, addedIndex)
        log.add(Node(operation.value, next))
        prev.next = normalizeNext(j, addedIndex)
    }
}

fun calculateNext(prevIndex: Int, prevNext: Next, addedIndex: Int): Next {
    val targetIndex = when (prevNext) {
        is Next.Increment1 -> prevIndex + 1
        is Next.Index -> prevNext.idx
        is Next.End -> return prevNext
    }

    return normalizeNext(addedIndex, targetIndex)
}

fun normalizeNext(sourceIndex: Int, destinationIndex: Int): Next = when (destinationIndex - sourceIndex) {
    1 -> Next.Increment1
    else -> Next.Index(destinationIndex)
}
