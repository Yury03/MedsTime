package com.example.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meds_track_database")
data class MedsTrackEntity(
    @PrimaryKey val id: String,
    val name: String,
    val endDate: Long,
    val packageCounter: Int,
    val recommendedPurchaseDate: Long,
    val packageItems: String,
    val stockOfMedicine: Double,
    val numberOfDays: Int,
    val trackType: String,
)