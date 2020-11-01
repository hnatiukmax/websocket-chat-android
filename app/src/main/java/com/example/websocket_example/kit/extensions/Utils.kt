package com.example.websocket_example.kit.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.Toast
import org.joda.time.DateTime

val DateTime.eventTimeFormat: String
    get() = toString("E d, H:m:s")

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}