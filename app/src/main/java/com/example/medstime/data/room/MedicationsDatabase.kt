package com.example.medstime.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medstime.data.room.dao.MedicationDao
import com.example.medstime.data.room.entity.MedicationEntity

@Database(entities = [MedicationEntity::class], version = 1)
abstract class MedicationsDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
}