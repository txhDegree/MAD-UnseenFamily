package com.example.unseenfamily.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unseenfamily.R
import com.example.unseenfamily.entities.Message

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var dataSet = emptyList<Message>()

    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val userMessage: TextView = view.findViewById<TextView>(R.id.textViewChatMessageUser)
        val adminMessage: TextView = view.findViewById<TextView>(R.id.textViewChatMessageAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = dataSet[position]
        if(message.from == "user"){
            holder.userMessage.text = message.content
            holder.adminMessage.visibility = View.INVISIBLE
        } else {
            holder.adminMessage.text = message.content
            holder.userMessage.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    internal fun setChat(messages: List<Message>) {
        dataSet = messages
        notifyDataSetChanged()
    }

}