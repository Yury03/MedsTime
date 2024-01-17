package com.example.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.room.dao.MedicationDao
import com.example.data.room.entity.MedicationEntity

@TypeConverters(
    MedicationEntity.IntPairListConverter::class,
    MedicationEntity.IntListConverter::class

)
@Database(entities = [MedicationEntity::class], version = 1)
abstract class MedicationDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    companion object {

        @Volatile
        private var INSTANCE: MedicationDatabase? = null
        fun getDatabase(context: Context): MedicationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MedicationDatabase::class.java, "medication_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

