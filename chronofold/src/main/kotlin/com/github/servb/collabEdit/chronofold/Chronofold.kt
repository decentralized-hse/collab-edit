package com.github.servb.collabEdit.chronofold

data class Node(
    val value: Value,
    val auth: String,
    val andx: Int,
    val next: Next,
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

    data class Index(val auth: String, val andx: Int) : Next()

    object End : Next()
}

class Chronofold private constructor(private val log: List<Node>) {

    constructor(vararg nodes: Node) : this(nodes.toList())

    private fun getIdByIndex(index: Next.Index): Int {
        // todo: get rid of linear search
        val result = log.indexOfFirst { it.andx == index.andx && it.auth == index.auth }
        check(result in log.indices) { "bad result ($result) for $index" }
        return result
    }

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
            when (currentNode.next) {
                is Next.Increment1 -> ++logIdx
                is Next.Index -> logIdx = getIdByIndex(currentNode.next)
                is Next.End -> break
            }
        }
        return result.toString()
    }
}
