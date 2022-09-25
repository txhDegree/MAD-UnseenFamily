package com.example.unseenfamily.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.unseenfamily.dao.DonationDao
import com.example.unseenfamily.dao.DonationItemDao
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationItem
import com.example.unseenfamily.entities.DonationWithDonationItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*


class DonationRepository (private val donationDao: DonationDao, private val donationItemDao: DonationItemDao) {

    val allDonation: LiveData<List<DonationWithDonationItems>> = donationDao.getAll()
    val pendingDonation: LiveData<List<DonationWithDonationItems>> = donationDao.getPending()
    val completeDonation: LiveData<List<DonationWithDonationItems>> = donationDao.getComplete()

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @WorkerThread
    suspend fun insert(donationWithDonationItems: DonationWithDonationItems){
        val firebaseAuth = FirebaseAuth.getInstance()
        with(donationWithDonationItems){
            var donationMap = HashMap<String, Any>()
            donationMap["title"] = donation.title
            donationMap["description"] = donation.description
            donationMap["family"] = firebaseAuth.currentUser!!.uid
            donationMap["status"] = donation.status

            var tempDonationItemList = emptyList<HashMap<String,Any>>().toMutableList()

            donationItems.forEach{
                tempDonationItemList.add(it.toHashMap())
            }

            donationMap["donationItems"] = tempDonationItemList.toList()
            if (donation.firebaseId.isNullOrBlank())
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
            else {
                val donationId = donationDao.insert(donation)
                donationItems.forEach{ donationItem ->
                    donationItem.donationId = donationId
                    donationItemDao.insert(donationItem)
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

    @WorkerThread
    suspend fun deleteAll(){
        donationDao.deleteAll()
    }

    @WorkerThread
    suspend fun findByTitle(title: String): DonationWithDonationItems {
        return donationDao.findByTitle(title)
    }

    @WorkerThread
    suspend fun reload(){

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        db.collection("donation")
            .whereEqualTo("family", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { docs ->
                GlobalScope.launch {
                    deleteAll()
                    for(doc in docs){
                        val title = doc.getString("title")!!
                        val desc = doc.getString("description")!!
                        val status = doc.getString("status")!!
                        val family = doc.getString("family")!!
                        val donationItemsList = doc.get("donationItems") as List<Any>
                        val donationItems = emptyList<DonationItem>().toMutableList()
                        donationItemsList.forEach{ donationItemMap ->
                            donationItemMap as Map<String, Any>
                            donationItemMap.forEach{
                                donationItems.add(DonationItem(it.value.toString()))
                            }
                        }
                        insert(DonationWithDonationItems(Donation(0,title,desc,family, status, doc.id),donationItems))
                    }
                }
            }

    }
}