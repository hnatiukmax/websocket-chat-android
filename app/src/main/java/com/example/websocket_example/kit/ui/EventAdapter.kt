package com.example.websocket_example.kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_example.R
import com.example.websocket_example.WebSocketEvent
import com.example.websocket_example.databinding.ItemEventBinding
import com.example.websocket_example.kit.extensions.eventTimeFormat

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var items = emptyList<WebSocketEvent>()

    fun addItem(item: WebSocketEvent) {
        items = items.plus(item)
        //notifyItemInserted(items.lastIndex)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding by lazy { ItemEventBinding.bind(itemView) }

        fun bind(item: WebSocketEvent) = with(binding) {
            (root as ViewGroup).children.forEach {
                val textColor = itemView.context.getColor(getColor(item))
                (it as? TextView)?.setTextColor(textColor)
            }

            event.text = itemView.context.getString(R.string.event_name, item.eventType)
            time.text = itemView.context.getString(R.string.event_time, item.time.eventTimeFormat)

            getDataMessage(item)?.let {
                message.text = itemView.context.getString(R.string.event_message, it)
            } ?: {
                message.isVisible = false
            }.invoke()
        }

        private fun getDataMessage(event: WebSocketEvent) = when (event) {
            is WebSocketEvent.TextMessage -> event.message
            is WebSocketEvent.Failure -> event.throwable.message.orEmpty()
            is WebSocketEvent.Open -> event.url
            else -> null
        }

        private fun getColor(event: WebSocketEvent) = when (event) {
            is WebSocketEvent.Failure -> android.R.color.holo_red_light
            is WebSocketEvent.Open -> android.R.color.holo_green_dark
            is WebSocketEvent.TextMessage -> android.R.color.holo_blue_dark
            is WebSocketEvent.ByteMessage -> android.R.color.holo_blue_dark
            is WebSocketEvent.Closing -> android.R.color.holo_orange_dark
            is WebSocketEvent.Closed -> android.R.color.holo_orange_dark
        }
    }
}