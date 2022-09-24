package com.example.unseenfamily.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.unseenfamily.entities.DonationItem

@Dao
interface DonationItemDao {

    // All record
    @Query("SELECT * FROM donationItem")
    fun getAll(): LiveData<List<DonationItem>>
    // Single record by title
    @Query("SELECT * FROM donationItem WHERE donationId = :donationId")
    fun findByDonationId(donationId: Long): LiveData<List<DonationItem>>
    // Create record
    @Insert
    suspend fun insert (donationItem: DonationItem)
    // Update record
    @Update
    suspend fun update (donationItem: DonationItem)
    // Delete record
    @Delete
    suspend fun delete (donationItem: DonationItem)

}