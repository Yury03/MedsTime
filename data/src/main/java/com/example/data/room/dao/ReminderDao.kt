package com.example.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.room.entity.ReminderEntity


@Dao
interface ReminderDao {
    @Query("SELECT * FROM medication_reminder_database")
    fun getAll(): List<ReminderEntity>

    @Query("SELECT * FROM medication_reminder_database WHERE id = :id")
    fun getById(id: String): ReminderEntity

    @Query("DELETE FROM medication_reminder_database WHERE id = :id")
    fun deleteById(id: String)

    @Insert
    fun insert(reminder: ReminderEntity)

    @Query("DELETE FROM medication_reminder_database WHERE medicationIntakeId = :medicationIntakeModelId")
    fun deleteByMedicationIntakeModelId(medicationIntakeModelId: String)

    @Query("SELECT * FROM medication_reminder_database WHERE status = :status")
    fun getWithStatus(status: String): List<ReminderEntity>
}