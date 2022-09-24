package com.example.unseenfamily.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unseenfamily.dao.DonationDao
import com.example.unseenfamily.dao.DonationItemDao
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationItem

@Database(entities = [Donation::class, DonationItem::class], version = 1, exportSchema = false)
abstract class DonationDB: RoomDatabase() {

    abstract fun donationDao(): DonationDao
    abstract fun donationItemDao(): DonationItemDao

    companion object{
        @Volatile
        private var INSTANCE : DonationDB? = null

        fun getDB(context: Context): DonationDB{
            val tempInstance = INSTANCE
            if(tempInstance!= null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DonationDB::class.java,
                    "donation_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }

    }

}