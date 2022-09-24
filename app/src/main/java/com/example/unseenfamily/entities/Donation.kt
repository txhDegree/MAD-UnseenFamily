package com.example.unseenfamily.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Donation (
    @PrimaryKey(autoGenerate = true) var id: Long,
    var title: String,
    var description: String,
    var family: String,
    var status:String = "n",
    var firebaseId: String? = null ) {

    constructor(
        title: String,
        description: String,
        family: String,
        donationItems: List<DonationItem>
    ): this(0, title, description, family, "n", null)

    constructor(
        title: String,
        description: String,
        family: String
    ): this(0, title, description, family, "n", null )
}