package com.github.servb.collabEdit.client

import com.github.servb.collabEdit.chronofold.*
import com.github.servb.collabEdit.client.externalDeclaration.diffMatchPatch.DiffMatchPatch

private val dmp = DiffMatchPatch()

// todo: multiple separated symbols
fun diff(newText: String, chronofold: Chronofold, ct: CausalTree, author: String): List<Operation> {
    val oldText = chronofold.getString()
    val diff = dmp
        .diff_main(oldText, newText)
        .unsafeCast<Array<Array<Any>>>()
        .map { (op, value) -> op.unsafeCast<Int>() to value.unsafeCast<String>() }
    val textTimestamps = chronofold.getTimestamps(ct)

    val result = mutableListOf<Operation>()

    var curIdx = 0
    diff.forEach { (op, value) ->
        when (op) {
            0 -> curIdx += value.length
            1 -> {
                var ts = Timestamp(author, ct.size + result.size)
                var ref = when (curIdx) {
                    0 -> ct.ndxInv(0)
                    else -> textTimestamps[curIdx - 1]
                }
                value.forEach { c ->
                    result.add(Operation(ts, ref, Value.Symbol(c)))
                    ref = ts
                    ts = Timestamp(author, ct.size + result.size)
                }
            }
            -1 -> {
                var ts = Timestamp(author, ct.size + result.size)
                var ref = textTimestamps[curIdx + value.length - 1]
                repeat(value.length) {
                    result.add(Operation(ts, ref, Value.Tombstone))
                    ref = ts
                    ts = Timestamp(author, ct.size + result.size)
                }
            }
        }
    }

    return result
}
