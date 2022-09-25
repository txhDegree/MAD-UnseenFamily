package com.example.unseenfamily.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.unseenfamily.entities.Message
import com.example.unseenfamily.repositories.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel (application: Application) : AndroidViewModel (application) {

    var messages : MutableLiveData<MutableList<Message>>

    private var chatRepository: ChatRepository = ChatRepository()

    init {
        messages = chatRepository.allMessage
    }

    fun send(message: Message) = viewModelScope.launch {
        chatRepository.send(message)
    }

    fun destroyListener() = viewModelScope.launch {
        chatRepository.destroyListener()
    }


}