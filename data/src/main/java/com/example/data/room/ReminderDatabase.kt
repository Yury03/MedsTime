package com.example.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.room.dao.ReminderDao
import com.example.data.room.entity.MedicationIntakeEntity


@Database(entities = [MedicationIntakeEntity::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDatabase? = null
        fun getDatabase(context: Context): ReminderDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ReminderDatabase::class.java, "medication_reminder_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}