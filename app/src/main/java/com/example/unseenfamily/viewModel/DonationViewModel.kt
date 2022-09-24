package com.example.unseenfamily.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.unseenfamily.db.DonationDB
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationItem
import com.example.unseenfamily.entities.DonationWithDonationItems
import com.example.unseenfamily.repositories.DonationRepository
import kotlinx.coroutines.launch

class DonationViewModel (application: Application) : AndroidViewModel (application) {

    var donationList : LiveData<List<DonationWithDonationItems>>
    var pendingDonationList : LiveData<List<DonationWithDonationItems>>
    var completeDonationList : LiveData<List<DonationWithDonationItems>>
    var selectedDonation: DonationWithDonationItems = DonationWithDonationItems(Donation("","",""), emptyList<DonationItem>())
    var viewOnly: Boolean = false
    private val donationRepository : DonationRepository

    init {
        val db = DonationDB.getDB(application)
        val donationDao = db.donationDao()
        val donationItemDao = db.donationItemDao()
        donationRepository = DonationRepository(donationDao, donationItemDao)
        donationList = donationRepository.allDonation
        pendingDonationList = donationRepository.pendingDonation
        completeDonationList = donationRepository.completeDonation
    }

    fun insert(donationWithDonationItems: DonationWithDonationItems) = viewModelScope.launch {
        donationRepository.insert(donationWithDonationItems)
    }

    fun update(donation: Donation) = viewModelScope.launch {
        donationRepository.update(donation)
    }

    fun delete(donation: Donation) = viewModelScope.launch {
        donationRepository.delete(donation)
    }

//    fun syncContact(){
//        donationRepository.syncContact("user1", contactList.value!!.toList())
//    }

}