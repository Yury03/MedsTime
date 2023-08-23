package com.example.medstime.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medstime.data.room.dao.MedicationIntakeDao
import com.example.medstime.data.room.entity.MedicationIntakeEntity

@TypeConverters(MedicationIntakeEntity.IntPairConverter::class)

@Database(entities = [MedicationIntakeEntity::class], version = 1)
abstract class MedicationIntakeDatabase : RoomDatabase() {
    abstract fun medicationIntake(): MedicationIntakeDao

    companion object {
        @Volatile
        private var INSTANCE: MedicationIntakeDatabase? = null
        fun getDatabase(context: Context): MedicationIntakeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MedicationIntakeDatabase::class.java, "medication_intake_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}