package com.example.unseenfamily.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.unseenfamily.dao.DonationItemDao
import com.example.unseenfamily.entities.DonationItem

class DonationItemRepository (private val donationItemDao: DonationItemDao) {

    val allDonationItem: LiveData<List<DonationItem>> = donationItemDao.getAll()

    @WorkerThread
    suspend fun insert(donationItem: DonationItem){
        donationItemDao.insert(donationItem)
    }

    @WorkerThread
    suspend fun update(donationItem: DonationItem){
        donationItemDao.update(donationItem)
    }

    @WorkerThread
    suspend fun delete(donationItem: DonationItem){
        donationItemDao.delete(donationItem)
    }

    @WorkerThread
    suspend fun deleteAll(){
        donationItemDao.deleteAll()
    }

    suspend fun findByDonationId(donationId: Long): LiveData<List<DonationItem>> {
        return donationItemDao.findByDonationId(donationId)
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