package com.example.unseenfamily

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.unseenfamily.databinding.FragmentChatBinding
import com.example.unseenfamily.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser!!
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val getProfilePic = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){
            binding.imageViewProfileImage.setImageURI(uri)
        }
    }

    private fun uploadProfilePic(){
        val bitmapDrawable = binding.imageViewProfileImage.drawable as BitmapDrawable

        val bitmap = bitmapDrawable.toBitmap()
        val profileRef = storage.reference.child(user.uid+".png")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = profileRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(context, "Fail To Upload Profile Picture", Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(context, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show()
                task.result.storage.downloadUrl.addOnSuccessListener { uri ->
                    user.updateProfile(userProfileChangeRequest {
                        photoUri = uri
                    }).addOnCompleteListener{ updateTask ->
                        if(updateTask.isSuccessful)
                            Toast.makeText(context, "Profile Picture Updated", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextName.setText(user.displayName)
        val profile = firestore.collection("profile").document(user.uid)
        profile.get().addOnSuccessListener { snapshot ->
            if(!snapshot.exists() || snapshot.data == null )
                return@addOnSuccessListener
            binding.editTextNumberFamily.setText(snapshot.data!!["family_size"].toString())
            binding.editTextPhone.setText(snapshot.data!!["contact_no"].toString())
        }

        if(!user.photoUrl.toString().isNullOrBlank()){
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var image: Bitmap? = null
            executor.execute{
                val imageUrl = user.photoUrl.toString()
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        binding.imageViewProfileImage.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.imageViewProfileImage.setOnClickListener {
            getProfilePic.launch("image/*")
        }

        binding.buttonUploadProfileImage.setOnClickListener{
            uploadProfilePic()
        }

        binding.buttonSaveProfile.setOnClickListener{
            val familySize = binding.editTextNumberFamily.text.toString().toInt()
            val contactNo = binding.editTextPhone.text.toString()
            firestore.collection("profile").document(user.uid).update(
                hashMapOf(
                    "family_size" to familySize,
                    "contact_no" to contactNo
                ) as Map<String, Any>
            ).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }

            val name = binding.editTextName.text.toString()
            if(user.displayName != name){
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user.updateProfile(profileUpdates)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(context, "Name Updated Successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_sync).isVisible = false
        menu.findItem(R.id.action_message).isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}