package com.example.websocket_example.kit.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_example.databinding.ItemMessageOtherBinding
import com.example.websocket_example.kit.model.Message

class OtherMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding by lazy { ItemMessageOtherBinding.bind(itemView) }

    fun bind(item: Message) = with(binding) {
        name.text = item.sender
        message.text = item.value
    }
}