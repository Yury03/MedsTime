package com.example.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.room.dao.MedsTrackDao
import com.example.data.room.entity.MedsTrackEntity


@Database(entities = [MedsTrackEntity::class], version = 1)
abstract class MedsTrackDatabase : RoomDatabase() {
    abstract fun medsTrackDao(): MedsTrackDao

    companion object {
        @Volatile
        private var INSTANCE: MedsTrackDatabase? = null
        fun getDatabase(context: Context): MedsTrackDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MedsTrackDatabase::class.java, "meds_track_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}