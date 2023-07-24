package com.example.medstime.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.medstime.data.room.entity.MedicationEntity

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications")
    fun getAll(): List<MedicationEntity>

    @Insert
    fun insert(medication: MedicationEntity)

    @Delete
    fun delete(medication: MedicationEntity)

    @Query("DELETE FROM medications WHERE id = :medicationId")
    fun deleteById(medicationId: String)
}