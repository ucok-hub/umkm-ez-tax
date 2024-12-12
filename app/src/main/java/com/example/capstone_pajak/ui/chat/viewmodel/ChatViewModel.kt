package com.example.capstone_pajak.ui.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstone_pajak.ui.chat.model.ChatMessage

class ChatViewModel : ViewModel() {
    private val _chatMessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val chatMessages: LiveData<MutableList<ChatMessage>> = _chatMessages

    fun addMessage(message: ChatMessage) {
        val currentMessages = _chatMessages.value ?: mutableListOf()
        currentMessages.add(message)
        _chatMessages.value = currentMessages
    }
}
