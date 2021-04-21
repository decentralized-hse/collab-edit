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
                val c = value.single()  // todo: support multiple sequential symbols
                val ts = Timestamp(author, ct.size + result.size)
                val ref = textTimestamps[curIdx - 1]  // todo: insertion to the start
                result.add(Operation(ts, ref, Value.Symbol(c)))
            }
            -1 -> {
                require(value.length == 1)  // todo: support multiple sequential symbols
                val ts = Timestamp(author, ct.size + result.size)
                val ref = textTimestamps[curIdx]
                result.add(Operation(ts, ref, Value.Tombstone))
            }
        }
    }

    return result
}
