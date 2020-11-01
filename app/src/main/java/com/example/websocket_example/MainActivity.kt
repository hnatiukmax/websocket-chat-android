package com.example.websocket_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.websocket_example.WebSocketEvent.*
import com.example.websocket_example.databinding.ActivityMainBinding
import com.example.websocket_example.kit.model.*
import com.example.websocket_example.kit.ui.ChatAdapter
import com.example.websocket_example.kit.ui.EventAdapter
import net.cachapa.expandablelayout.ExpandableLayout
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    /**
     * UI
     */
    private val chatAdapter by lazy { ChatAdapter() }
    private val eventAdapter by lazy { EventAdapter() }

    /**
     * Network
     */
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private lateinit var webSocket: WebSocket
    private lateinit var socketListener: SocketListener
    private var user: User? = null

    companion object {
        private const val baseUrl = "10.0.2.2:5000"
        private const val webSocketUrl = "ws://$baseUrl/chat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
            initUI()
        }

        initWebSocket()
    }

    override fun onStop() {
        super.onStop()
        webSocket.close(1000, null)
    }

    private fun initUI() = with(binding) {
        events.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = eventAdapter
        }

        chat.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = chatAdapter
        }

        send.setOnClickListener {
            val messageText = messageField.text.toString()
            messageField.setText("")
            webSocket.send(messageText)
        }

        eventDropContainer.setOnClickListener {
            if (eventContainer.state == ExpandableLayout.State.EXPANDING || eventContainer.state == ExpandableLayout.State.COLLAPSING) {
                return@setOnClickListener
            }
            val degree = if (eventContainer.isExpanded) 0f else 180f
            eventContainer.isExpanded = !eventContainer.isExpanded
            drop.animate().rotation(degree).setDuration(eventContainer.duration.toLong()).start()
        }
    }

    private fun initWebSocket() {
        socketListener = SocketListener()
        client = OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .build()
        request = Request.Builder().url(webSocketUrl).build()
        webSocket = client.newWebSocket(request, socketListener)
    }

    private operator fun EventAdapter.plus(event: WebSocketEvent) = runOnUiThread {
        eventAdapter.addItem(event)
    }

    private inner class SocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            eventAdapter + Open(response.request.url.toString())
        }

        override fun onMessage(webSocket: WebSocket, text: String) = runOnUiThread {
            eventAdapter + TextMessage(text)
            handleTextMessage(text.asEvent)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            eventAdapter + ByteMessage()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            eventAdapter + Closing()
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            eventAdapter + Closed()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            eventAdapter + Failure(t)
        }
    }

    private fun handleTextMessage(event: Event) = when(event.type) {
        Event.GREETING -> user = event.value.asUser
        Event.NEW_USER -> {
            chatAdapter.addItem(event.value.asUser.asStateHolderNewUser)
            scrollChatToBottom()
        }
        Event.MESSAGE -> {
            val message = event.value.asMessage
            if (message.sender == user?.name) {
                chatAdapter.addItem(message.asStateHolderOwnMessage)
            } else {
                chatAdapter.addItem(message.asStateHolderOtherMessage)
            }
            scrollChatToBottom()
        }
        else -> Unit
    }

    private fun scrollChatToBottom() = binding.chat.run {
        post { smoothScrollToPosition(chatAdapter.itemCount - 1) }
        Unit
    }
}