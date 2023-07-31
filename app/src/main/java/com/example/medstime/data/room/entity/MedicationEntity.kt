package com.example.medstime.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,//todo ColumnInfo?
    @ColumnInfo val dosage: Double,
    @ColumnInfo val dosageUnit: String,
    @ColumnInfo val intakeTimes: List<Pair<Int, Int>>,
    @ColumnInfo val reminderTime: Int,
    @ColumnInfo val frequency: String,
    @ColumnInfo val selectedDays: List<Int>?,
    @ColumnInfo val intakeType: String,
    @ColumnInfo val startDate: String,
    @ColumnInfo val endDate: String?,

)




