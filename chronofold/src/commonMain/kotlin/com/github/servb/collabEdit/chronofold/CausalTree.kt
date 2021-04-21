package com.github.servb.collabEdit.chronofold

data class Timestamp(
    val author: String,
    val authorIndex: Int,
)

data class Operation(
    val timestamp: Timestamp,
    val ref: Timestamp,
    val value: Value,
)

class CausalTree private constructor(private val log: MutableList<Operation>) {

    constructor(vararg operations: Operation) : this(operations.toMutableList())

    val size: Int get() = log.size

    fun ndx(timestamp: Timestamp): Int {
        // ineffective:
        // return log.indexOfFirst { it.timestamp == timestamp }.let { if (it == -1) Int.MAX_VALUE else it }

        var idx = timestamp.authorIndex  // starting from authorIndex for optimization

        while (idx < log.size && log[idx].timestamp != timestamp) {
            ++idx
        }

        return when (idx == log.size) {
            true -> Int.MAX_VALUE
            false -> idx
        }
    }

    fun ndxInv(ndx: Int): Timestamp = log[ndx].timestamp

    fun add(operation: Operation) {
        log.add(operation)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CausalTree

        if (log != other.log) return false

        return true
    }

    override fun hashCode(): Int {
        return log.hashCode()
    }

    override fun toString(): String {
        return "CausalTree(${log.joinToString("\n", "\n", "\n")})"
    }
}
