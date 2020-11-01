package com.example.websocket_example.kit.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_example.R
import com.example.websocket_example.kit.model.User

class NewUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: User) {
        itemView.findViewById<TextView>(R.id.name).text = itemView.context.getString(R.string.user_connected, item.name)
    }
}