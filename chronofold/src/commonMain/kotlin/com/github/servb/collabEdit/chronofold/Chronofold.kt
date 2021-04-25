package com.github.servb.collabEdit.chronofold

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Node(
    val value: Value,
    var next: Next,
)

@Serializable
sealed class Value {

    /** ∅. */
    @Serializable
    @SerialName("r")
    object Root : Value() {

        override fun toString(): String = "∅"
    }

    // todo: replace Char (uint16) with UInt (uint32)
    @Serializable
    @SerialName("s")
    data class Symbol(val char: Char) : Value()

    /** ⌫. */
    @Serializable
    @SerialName("t")
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

    private fun iterateByNext(onSymbol: (char: Char, logIdx: Int) -> Unit, onTombstone: () -> Unit) {
        var logIdx = 0
        while (true) {
            val currentNode = log[logIdx]
            when (currentNode.value) {
                is Value.Root -> {
                }
                is Value.Symbol -> onSymbol(currentNode.value.char, logIdx)
                is Value.Tombstone -> onTombstone()
            }
            when (val next = currentNode.next) {
                is Next.Increment1 -> ++logIdx
                is Next.Index -> logIdx = next.idx
                is Next.End -> break
            }
        }
    }

    fun getString(): String {
        val result = StringBuilder()
        iterateByNext(
            onSymbol = { char, _ -> result.append(char) },
            onTombstone = { result.deleteAt(result.lastIndex) }
        )
        return result.toString()
    }

    fun getTimestamps(ct: CausalTree): List<Timestamp> {
        val result = mutableListOf<Timestamp>()
        iterateByNext(
            onSymbol = { _, logIdx -> result.add(ct.ndxInv(logIdx)) },
            onTombstone = { result.removeLast() }
        )
        return result
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Chronofold

        if (log != other.log) return false

        return true
    }

    override fun hashCode(): Int {
        return log.hashCode()
    }

    override fun toString(): String {
        val logWithIndices = log
            .withIndex()
            .joinToString("\n", "\n", "\n") { (i, it) -> "$i: $it" }
        return "Chronofold($logWithIndices)"
    }
}
