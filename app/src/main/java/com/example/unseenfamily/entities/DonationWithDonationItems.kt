package com.example.unseenfamily.entities

import androidx.room.Embedded
import androidx.room.Relation


data class DonationWithDonationItems (
    @Embedded val donation: Donation,
    @Relation(
        parentColumn = "id",
        entityColumn = "donationId"
    )
    val donationItems: List<DonationItem>
)