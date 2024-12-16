package com.example.capstone_pajak.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.chat.adapter.ChatAdapter
import com.example.capstone_pajak.ui.chat.model.ChatMessage
import com.example.capstone_pajak.ui.chat.viewmodel.ChatViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class ChatRoomFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        val chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_recycler_view)
        val messageInput = view.findViewById<EditText>(R.id.et_message_input)
        val sendButton = view.findViewById<ImageView>(R.id.btn_send)
        val backButton = view.findViewById<ImageView>(R.id.btn_back)

        // Set up RecyclerView
        val chatAdapter = ChatAdapter(mutableListOf())
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = chatAdapter

        // Observe chat messages
        viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.updateMessages(messages)
            chatRecyclerView.scrollToPosition(messages.size - 1)
        }

        // Handle send button click
        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString()
            if (userMessage.isNotBlank()) {
                // Add user message
                val userChatMessage = ChatMessage(userMessage, isUser = true)
                viewModel.addMessage(userChatMessage)

                // Get AI response using API
                getAIResponse(userMessage) { aiResponse ->
                    val aiChatMessage = ChatMessage(aiResponse, isUser = false)
                    viewModel.addMessage(aiChatMessage)
                }

                messageInput.text.clear()
            }
        }

        // Handle back button click
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    private fun getAIResponse(input: String, callback: (String) -> Unit) {
        val url = "https://diagflowapi-1071674645783.asia-southeast2.run.app/chat" // Updated API endpoint
        val json = JSONObject()
        try {
            // Create JSON body as per API specification
            json.put("message", input)
            json.put("language_code", "id") // Assuming Indonesian language code

            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            // Build request
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            // Make asynchronous request
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("ChatRoomFragment", "Network error: ${e.message}")
                    requireActivity().runOnUiThread {
                        callback("Error: Network failure.")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    requireActivity().runOnUiThread {
                        try {
                            if (!response.isSuccessful) {
                                Log.e("ChatRoomFragment", "Error: ${response.code}")
                                callback("Error: Unable to get response from server.")
                                return@runOnUiThread
                            }

                            val responseBody = response.body?.string()
                            if (responseBody.isNullOrEmpty()) {
                                Log.e("ChatRoomFragment", "Error: Response body is null or empty")
                                callback("Error: Invalid response from server.")
                                return@runOnUiThread
                            }

                            // Parse response
                            val answer = parseResponse(responseBody)
                            callback(answer)
                        } catch (e: Exception) {
                            Log.e("ChatRoomFragment", "Unexpected error: ${e.message}")
                            callback("Error: Unexpected error occurred.")
                        } finally {
                            response.close()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.e("ChatRoomFragment", "Unexpected error in request setup: ${e.message}")
            requireActivity().runOnUiThread {
                callback("Error: Failed to send request.")
            }
        }
    }

    private fun parseResponse(response: String): String {
        return try {
            // Extract "reply" array from JSON and join it into a single response
            val jsonResponse = JSONObject(response)
            val repliesArray = jsonResponse.optJSONArray("reply")
            if (repliesArray != null && repliesArray.length() > 0) {
                val responses = mutableListOf<String>()
                for (i in 0 until repliesArray.length()) {
                    responses.add(repliesArray.getString(i))
                }
                responses.joinToString("\n") // Combine multiple responses
            } else {
                "I'm not sure how to respond."
            }
        } catch (e: Exception) {
            Log.e("ChatRoomFragment", "Error parsing response: ${e.message}")
            "Error: Unable to process response."
        }
    }
}
