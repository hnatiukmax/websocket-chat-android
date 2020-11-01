package com.example.websocket_example.kit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_example.R
import com.example.websocket_example.kit.model.Message
import com.example.websocket_example.kit.model.User

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val OWN_MESSAGE = 1
        private const val OTHER_MESSAGE = 2
        private const val NEW_USER = 3
    }

    private var items = emptyList<StateHolder<*>>()

    fun submitItems(items: List<StateHolder<*>>) {
        this.items = items
        notifyItemInserted(items.lastIndex)
    }

    fun addItem(item: StateHolder<*>) {
        items = items.plus(item)
        notifyItemInserted(items.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            OWN_MESSAGE -> OwnMessageViewHolder(inflater.inflate(R.layout.item_message_own, parent, false))
            OTHER_MESSAGE -> OtherMessageViewHolder(inflater.inflate(R.layout.item_message_other, parent, false))
            NEW_USER -> NewUserViewHolder(inflater.inflate(R.layout.item_new_user, parent, false))
            else -> throw IllegalArgumentException("Unsupported item with viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when(holder) {
            is OwnMessageViewHolder -> (item.value as? Message)?.let { holder.bind(it) }
            is OtherMessageViewHolder -> (item.value as? Message)?.let { holder.bind(it) }
            is NewUserViewHolder -> (item.value as? User)?.let { holder.bind(it) }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when(val property = items[position].properties.firstOrNull()) {
        Property.OWN_MESSAGE -> OWN_MESSAGE
        Property.OTHER_MESSAGE -> OTHER_MESSAGE
        Property.NEW_USER -> NEW_USER
        else -> throw IllegalArgumentException("Unsupported item with property $property")
    }
}