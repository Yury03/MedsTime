package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.room.entity.MedsTrackEntity


@Dao
interface MedsTrackDao {

    @Query("SELECT * FROM meds_track_database")
    fun getAll(): List<MedsTrackEntity>

    @Insert
    fun insert(medsTrackEntity: MedsTrackEntity)

    @Query("SELECT * FROM meds_track_database WHERE id = :id")
    fun getById(id: String): MedsTrackEntity

    @Query("DELETE FROM meds_track_database WHERE id = :id")
    fun deleteById(id: String)
}