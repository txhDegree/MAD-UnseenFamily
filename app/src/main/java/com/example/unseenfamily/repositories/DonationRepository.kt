package com.example.unseenfamily.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.unseenfamily.dao.DonationDao
import com.example.unseenfamily.dao.DonationItemDao
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationWithDonationItems
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DonationRepository (private val donationDao: DonationDao, private val donationItemDao: DonationItemDao) {

    val allDonation: LiveData<List<DonationWithDonationItems>> = donationDao.getAll()
    val pendingDonation: LiveData<List<DonationWithDonationItems>> = donationDao.getPending()
    val completeDonation: LiveData<List<DonationWithDonationItems>> = donationDao.getComplete()

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @WorkerThread
    suspend fun insert(donationWithDonationItems: DonationWithDonationItems){
        with(donationWithDonationItems){
            var donationMap = HashMap<String, Any>()
            donationMap["title"] = donation.title
            donationMap["description"] = donation.description
            donationMap["family"] = "123"
            donationMap["status"] = donation.status

            var tempDonationItemList = emptyList<HashMap<String,Any>>().toMutableList()

            donationItems.forEach{
                tempDonationItemList.add(it.toHashMap())
            }

            donationMap["donationItems"] = tempDonationItemList.toList()

            db.collection("donation").add(donationMap)
                .addOnSuccessListener {
                    donation.firebaseId = it.id
                    GlobalScope.launch {
                        val donationId = donationDao.insert(donation)
                        donationItems.forEach{ donationItem ->
                            donationItem.donationId = donationId
                            donationItemDao.insert(donationItem)
                        }
                    }
                }
        }
    }

    @WorkerThread
    suspend fun update(donation: Donation){
        donationDao.update(donation)
    }

    @WorkerThread
    suspend fun delete(donation: Donation){
        donationDao.delete(donation)
    }

    suspend fun findByTitle(title: String): DonationWithDonationItems {
        return donationDao.findByTitle(title)
    }

//    fun syncContact(id: String, contactList: List<Donation>){
//        val db: DatabaseReference = Firebase.database.reference
//
//        for(contact in contactList.listIterator()){
//            var target = db.child("contact").child(id)
//            target.child("name").setValue(contact.name)
//            target.child("phone").setValue(contact.phone)
//        }
//    }

}