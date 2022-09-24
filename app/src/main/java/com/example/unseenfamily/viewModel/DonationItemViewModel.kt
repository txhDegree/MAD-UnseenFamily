package com.example.unseenfamily.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.unseenfamily.db.DonationDB
import com.example.unseenfamily.entities.DonationItem
import com.example.unseenfamily.repositories.DonationItemRepository
import kotlinx.coroutines.launch

class DonationItemViewModel (application: Application) : AndroidViewModel (application) {

    var donationItemList : LiveData<List<DonationItem>>

    private val donationItemRepository : DonationItemRepository

    init {
        val donationItemDao = DonationDB.getDB(application).donationItemDao()
        donationItemRepository = DonationItemRepository(donationItemDao)
        donationItemList = donationItemRepository.allDonationItem
    }

    fun insert(donationItem: DonationItem) = viewModelScope.launch {
        donationItemRepository.insert(donationItem)
    }

    fun update(donationItem: DonationItem) = viewModelScope.launch {
        donationItemRepository.update(donationItem)
    }

    fun delete(donationItem: DonationItem) = viewModelScope.launch {
        donationItemRepository.delete(donationItem)
    }

//    fun syncContact(){
//        donationRepository.syncContact("user1", contactList.value!!.toList())
//    }

}