package com.example.websocket_example.kit.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_example.R
import com.example.websocket_example.kit.model.Message

class OwnMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Message) {
        itemView.findViewById<TextView>(R.id.message).text = item.value
    }
}