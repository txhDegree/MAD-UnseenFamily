package com.example.unseenfamily.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationWithDonationItems

@Dao
interface DonationDao {

    // All record
    @Transaction
    @Query("SELECT * FROM donation")
    fun getAll(): LiveData<List<DonationWithDonationItems>>

    // Complete Record
    @Transaction
    @Query("SELECT * FROM donation WHERE status = 'y'")
    fun getComplete(): LiveData<List<DonationWithDonationItems>>

    // Pending Record
    @Transaction
    @Query("SELECT * FROM donation WHERE status = 'n'")
    fun getPending(): LiveData<List<DonationWithDonationItems>>

    // Single record by title
    @Transaction
    @Query("SELECT * FROM donation WHERE title LIKE :title")
    suspend fun findByTitle(title: String): DonationWithDonationItems
    // Create record
    @Insert
    suspend fun insert (donation: Donation): Long
    // Update record
    @Update
    suspend fun update (donation: Donation)
    // Delete record
    @Delete
    suspend fun delete (donation: Donation)

    // Delete all records
    @Query("DELETE FROM donation")
    suspend fun deleteAll ()

}