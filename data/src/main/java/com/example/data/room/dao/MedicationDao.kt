package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.data.room.entity.MedicationEntity

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medication_database")
    fun getAll(): List<MedicationEntity>

    @Insert
    fun insert(medication: MedicationEntity)

    @Delete
    fun delete(medication: MedicationEntity)

    @Query("SELECT * FROM medication_database WHERE id =:medicationId")
    fun getById(medicationId: String): MedicationEntity

    @Query("DELETE FROM medication_database WHERE id =:id")
    fun deleteById(id: String)

}