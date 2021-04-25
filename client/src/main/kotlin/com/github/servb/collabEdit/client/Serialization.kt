package com.github.servb.collabEdit.client

import com.github.servb.collabEdit.chronofold.Operation
import com.github.servb.collabEdit.chronofold.Timestamp
import com.github.servb.collabEdit.chronofold.Value
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val json = Json {
    this.serializersModule = SerializersModule {
        contextual(Operation.serializer())
        contextual(Timestamp.serializer())
        polymorphic(Value::class) {
            subclass(Value.Symbol::class)
            subclass(Value.Root::class)
            subclass(Value.Tombstone::class)
        }
    }
}
