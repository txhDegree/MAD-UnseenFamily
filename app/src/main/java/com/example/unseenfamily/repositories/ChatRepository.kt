package com.example.unseenfamily.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unseenfamily.dao.DonationItemDao
import com.example.unseenfamily.entities.DonationItem
import com.example.unseenfamily.entities.Message
import com.example.unseenfamily.viewModel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.lang.Exception

class ChatRepository {

    val allMessage = MutableLiveData<MutableList<Message>>()
    private var db: FirebaseFirestore
    private var firebaseAuth: FirebaseAuth
    private var listener: ListenerRegistration

    init {

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val docRef = db.collection("message").document(firebaseAuth.currentUser!!.uid)

        allMessage.value = emptyList<Message>().toMutableList()
        docRef.get().addOnSuccessListener { snapshot ->
            if(snapshot.data == null || snapshot.data!!["chats"] == null){
                allMessage.value!!.clear()
                return@addOnSuccessListener
            }
            val result = snapshot.data!!["chats"] as MutableList<HashMap<String, String>>
            val tempMessageList = emptyList<Message>().toMutableList()
            result.forEach {
                tempMessageList.add(Message(it["from"].toString(), it["content"].toString(),it["timestamp"].toString()))
            }
            allMessage.value = ( tempMessageList )
        }

        listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                "Local"
            else
                "Server"

            if (snapshot != null && snapshot.exists()) {
                if(snapshot.data == null || snapshot.data!!["chats"] == null){
                    allMessage.value = emptyList<Message>().toMutableList()
                    return@addSnapshotListener
                }
                val result = snapshot.data!!["chats"] as MutableList<HashMap<String, String>>
                val tempMessageList = emptyList<Message>().toMutableList()
                result.forEach {
                    tempMessageList.add(Message(it["from"].toString(), it["content"].toString(),it["timestamp"].toString()))
                }
//                allMessage.value!!.clear()
                allMessage.value = ( tempMessageList )
//                allMessage.value!!.addAll(tempMessageList)
                Log.d(TAG, "$source data: ${snapshot.data}")
            } else {
                Log.d(TAG, "$source data: null")
            }
        }
    }

    @WorkerThread
    suspend fun send(message: Message){
        val docRef = db.collection("message").document(firebaseAuth.currentUser!!.uid)
        docRef.get().addOnSuccessListener { result ->
            if(result.exists()){
                docRef.update("chats", FieldValue.arrayUnion(message)).addOnFailureListener{
                    Log.d(TAG+"ERR", it.message.toString())
                }
            } else{
                val list = emptyList<Message>().toMutableList()
                list.add(message)
                docRef.set(hashMapOf(
                    "chats" to list
                )).addOnCompleteListener{
                    Log.d(TAG+"COMPLETE", it.isSuccessful.toString())
                }
            }
        }
    }

    @WorkerThread
    suspend fun destroyListener(){
        listener.remove()
    }

    companion object {
        val TAG = "ChatRepo"
    }

}