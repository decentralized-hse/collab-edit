package com.github.servb.collabEdit.chronofold

data class Node(
    val value: Value,
    var next: Next,
)

sealed class Value {

    /** ∅. */
    object Root : Value()

    // todo: replace Char (uint16) with UInt (uint32)
    data class Symbol(val char: Char) : Value()

    /** ⌫. */
    object Tombstone : Value()
}

sealed class Next {

    object Increment1 : Next()

    data class Index(val idx: Int) : Next()

    object End : Next()
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
}
