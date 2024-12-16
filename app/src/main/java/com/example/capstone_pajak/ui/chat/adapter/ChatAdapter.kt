package com.example.capstone_pajak.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.chat.model.ChatMessage

class ChatAdapter(
    private var messages: MutableList<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    fun updateMessages(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        if (message.isUser) {
            holder.userMessage.text = message.message
            holder.userMessage.visibility = View.VISIBLE
            holder.aiMessage.visibility = View.GONE
        } else {
            holder.aiMessage.text = message.message
            holder.aiMessage.visibility = View.VISIBLE
            holder.userMessage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userMessage: TextView = itemView.findViewById(R.id.tv_user_message)
        val aiMessage: TextView = itemView.findViewById(R.id.tv_ai_message)
    }
}
