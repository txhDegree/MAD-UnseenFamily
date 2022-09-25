package com.example.unseenfamily

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unseenfamily.adapter.ChatAdapter
import com.example.unseenfamily.databinding.FragmentChatBinding
import com.example.unseenfamily.entities.Message
import com.example.unseenfamily.viewModel.ChatViewModel

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(false)
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewSend.setOnClickListener{
            val content = binding.editTextMessage.text
            if(content.isNullOrEmpty()){
                return@setOnClickListener
            }
            chatViewModel.send(Message(content.toString()))
            binding.editTextMessage.setText("")
        }
        val chatAdapter = ChatAdapter()

        chatViewModel.messages.observe(viewLifecycleOwner) {
            chatAdapter.setChat(it)
            binding.recycleViewMessages.layoutManager!!.scrollToPosition(it.size-1)
        }
        val llm = LinearLayoutManager(activity?.applicationContext)
        llm.stackFromEnd = true
        binding.recycleViewMessages.layoutManager = llm
        binding.recycleViewMessages.adapter = chatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatViewModel.destroyListener()
        _binding = null
    }

}