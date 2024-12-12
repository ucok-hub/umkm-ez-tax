package com.example.capstone_pajak.ui.chat

import android.content.res.AssetFileDescriptor
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
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ChatRoomFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel
    private var tfliteInterpreter: Interpreter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val modelPath = "optimized_model_predict.tflite"
            tfliteInterpreter = Interpreter(loadModelFile(modelPath))
            Log.d("ChatRoomFragment", "Model loaded successfully.")
        } catch (e: Exception) {
            Log.e("ChatRoomFragment", "Error loading model: ${e.message}")
        }
    }

    private fun loadModelFile(modelPath: String): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = requireContext().assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        val chatRecyclerView = view.findViewById<RecyclerView>(R.id.chat_recycler_view)
        val messageInput = view.findViewById<EditText>(R.id.et_message_input)
        val sendButton = view.findViewById<ImageView>(R.id.btn_send)
        val backButton = view.findViewById<ImageView>(R.id.btn_back)

        val chatAdapter = ChatAdapter(mutableListOf())
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = chatAdapter

        viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.updateMessages(messages)
            chatRecyclerView.scrollToPosition(messages.size - 1)
        }

        sendButton.setOnClickListener {
            val userMessage = messageInput.text.toString()
            if (userMessage.isNotBlank()) {
                val userChatMessage = ChatMessage(userMessage, isUser = true)
                viewModel.addMessage(userChatMessage)

                val aiResponse = getAIResponse(userMessage)
                val aiChatMessage = ChatMessage(aiResponse, isUser = false)
                viewModel.addMessage(aiChatMessage)

                messageInput.text.clear()
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    private fun preprocessInput(input: String): FloatArray {
        // Example preprocessing logic
        val processedInput = FloatArray(256) { 0f }
        // Fill with actual preprocessed values
        return processedInput
    }

    private fun getAIResponse(input: String): String {
        val interpreter = tfliteInterpreter
        if (interpreter == null) {
            Log.e("ChatRoomFragment", "Error: TensorFlow Lite model not initialized.")
            return "Error: Model not loaded."
        }

        val processedInput = preprocessInput(input)
        return try {
            val inputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 256), org.tensorflow.lite.DataType.FLOAT32)
            inputTensor.loadArray(processedInput)

            val outputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 5), org.tensorflow.lite.DataType.FLOAT32)
            interpreter.run(inputTensor.buffer, outputTensor.buffer.rewind())

            val outputArray = outputTensor.floatArray
            val predictedIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
            if (predictedIndex != -1) {
                val responses = arrayOf("Response1", "Response2", "Response3")
                responses[predictedIndex]
            } else {
                "I couldn't understand that."
            }
        } catch (e: Exception) {
            Log.e("ChatRoomFragment", "Error during classification: ${e.message}")
            "Error: Unable to process input."
        }
    }
}
