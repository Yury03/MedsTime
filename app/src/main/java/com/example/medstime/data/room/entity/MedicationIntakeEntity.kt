package com.example.medstime.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "medication_intake")
data class MedicationIntakeEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val dosage: Double,
    @ColumnInfo val dosageUnit: String,
    @ColumnInfo val isTaken: Boolean,
    @ColumnInfo val reminderTime: Int,
    @ColumnInfo val medicationId: Int,
    @ColumnInfo val intakeTime: Pair<Int, Int>,
    @ColumnInfo val actualIntakeTime: Pair<Int, Int>?,
    @ColumnInfo val intakeType: String,
)