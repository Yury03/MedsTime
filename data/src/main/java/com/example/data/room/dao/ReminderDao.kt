package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.room.entity.ReminderEntity


@Dao
interface ReminderDao {
    @Query("SELECT * FROM medication_reminder_database")
    fun getAll(): List<ReminderEntity>

    @Insert
    fun insert(medicationIntake: ReminderEntity)

    @Query("DELETE FROM medication_reminder_database WHERE medicationId = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM medication_reminder_database WHERE medicationIntakeId = :medicationIntakeModelId")
    fun deleteByMedicationModelId(medicationIntakeModelId: String)
}