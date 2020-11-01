package com.example.websocket_example.kit.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Event(val type: String, val value: String) {

    companion object {
        const val GREETING = "GREETING"
        const val MESSAGE = "MESSAGE"
        const val NEW_USER = "NEW_USER"
    }
}

val String.asEvent: Event
    get() = Json.decodeFromString(this)