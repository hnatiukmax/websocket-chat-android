package com.example.websocket_example

import org.joda.time.DateTime

sealed class WebSocketEvent(val eventType: String, val time: DateTime = DateTime.now()) {

    class Open(val url: String) : WebSocketEvent("OPEN")
    class TextMessage(val message: String) : WebSocketEvent("TEXT MESSAGE")
    class ByteMessage : WebSocketEvent("BYTE MESSAGE")
    class Closing : WebSocketEvent("CLOSING")
    class Closed : WebSocketEvent("CLOSED")
    class Failure(val throwable: Throwable) : WebSocketEvent("FAILURE")
}