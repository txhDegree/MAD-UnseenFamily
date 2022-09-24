package com.example.unseenfamily.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DonationItem (@PrimaryKey(autoGenerate = true) val id: Long, var name : String, var donationId: Long) {
    constructor(name: String) : this(0, name, 0)
    public fun toHashMap() : HashMap<String,Any>{
        var result = HashMap<String,Any>()
        result["name"] = name
        return result
    }
}