package com.example.medstime.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.medstime.data.room.entity.MedicationIntakeEntity

@Dao
interface MedicationIntakeDao {
    @Query("SELECT * FROM medication_intake_database")
    fun getAll(): List<MedicationIntakeEntity>

    @Insert
    fun insert(medicationIntake: MedicationIntakeEntity)

    @Query("DELETE FROM medication_intake_database WHERE medicationId = :id")
    fun deleteById(id: String)
    @Query("DELETE FROM medication_intake_database WHERE medicationId = :medicationModelId")
    fun deleteByMedicationModelId(medicationModelId: String)
}