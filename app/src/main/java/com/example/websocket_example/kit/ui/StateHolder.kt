package com.example.websocket_example.kit.ui

class StateHolder<T>(val value: T, val properties: Set<Property> = emptySet())

infix fun <T> StateHolder<T>.has(property: Property) = properties.contains(property)

enum class Property {
    OWN_MESSAGE,
    OTHER_MESSAGE,
    NEW_USER
}

fun <T> T.toStateHolder(property: Property) = StateHolder(this, setOf(property))