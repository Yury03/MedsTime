package com.example.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_reminder_database")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val medicationIntakeId: String,
    val type: String,
    val status: String,
    val timeShow: String,
)