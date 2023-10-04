package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.room.entity.MedicationIntakeEntity

@Dao
interface MedicationIntakeDao {
    @Query("SELECT * FROM medication_intake_database")
    fun getAll(): List<MedicationIntakeEntity>

    @Query("SELECT * FROM medication_intake_database WHERE id = :id")
    fun getById(id: String): MedicationIntakeEntity

    @Query("DELETE FROM medication_intake_database WHERE id = :id")
    fun deleteById(id: String)

    @Insert
    fun insert(medicationIntake: MedicationIntakeEntity)

    @Query("UPDATE medication_intake_database SET isTaken = :isTaken, actualIntakeTime = :actualIntakeTime WHERE id = :id")
    fun updateIsTakenById(id: String, isTaken: Boolean, actualIntakeTime: String?)

    @Query("DELETE FROM medication_intake_database WHERE medicationId = :medicationModelId")
    fun deleteByMedicationModelId(medicationModelId: String)
}