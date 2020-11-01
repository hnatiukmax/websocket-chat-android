package com.example.websocket_example.kit.model

import com.example.websocket_example.kit.ui.Property
import com.example.websocket_example.kit.ui.StateHolder
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class User(val name: String)

val String.asUser: User
    get() = Json.decodeFromString(this)

val User.asStateHolderNewUser get() = StateHolder(value = this, properties = setOf(Property.NEW_USER))