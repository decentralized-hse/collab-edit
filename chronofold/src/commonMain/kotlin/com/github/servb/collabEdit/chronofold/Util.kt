package com.github.servb.collabEdit.chronofold

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

fun incrementOrEnd(currentLength: Int, size: Int): Next = when (currentLength == size) {
    true -> Next.End
    false -> Next.Increment1
}

fun createInitialData(string: String, author: String): Pair<CausalTree, Chronofold> {
    val ctLog = mutableListOf<Operation>()
    val chronofoldLog = mutableListOf<Node>()

    ctLog.add(Operation(Timestamp(author, 0), Timestamp(author, 0), Value.Root))
    chronofoldLog.add(Node(Value.Root, incrementOrEnd(chronofoldLog.size, string.length)))

    string.forEachIndexed { index, c ->
        ctLog.add(Operation(Timestamp(author, index + 1), Timestamp(author, index), Value.Symbol(c)))
        chronofoldLog.add(Node(Value.Symbol(c), incrementOrEnd(chronofoldLog.size, string.length)))
    }

    return CausalTree(*ctLog.toTypedArray()) to Chronofold(*chronofoldLog.toTypedArray())
}
