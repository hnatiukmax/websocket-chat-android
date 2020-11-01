package com.example.websocket_example.kit.model

import com.example.websocket_example.kit.ui.Property
import com.example.websocket_example.kit.ui.StateHolder
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Message(val sender: String, val value: String)

val Message.asStateHolderOwnMessage get() = StateHolder(value = this, properties = setOf(Property.OWN_MESSAGE))
val Message.asStateHolderOtherMessage get() = StateHolder(value = this, properties = setOf(Property.OTHER_MESSAGE))

val Message.jsonString: String
    get() = Json.encodeToString(this)

val String.asMessage: Message
    get() = Json.decodeFromString(this)