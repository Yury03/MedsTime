package com.example.medstime.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medstime.data.room.dao.MedicationIntakeDao
import com.example.medstime.data.room.entity.MedicationIntakeEntity

@Database(entities = [MedicationIntakeEntity::class], version = 1)
abstract class MedicationIntakeDatabase : RoomDatabase() {
    abstract fun medicationIntake(): MedicationIntakeDao
}