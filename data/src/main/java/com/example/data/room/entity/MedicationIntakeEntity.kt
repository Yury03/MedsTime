package com.example.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters


@Entity(tableName = "medication_intake_database")
data class MedicationIntakeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val dosage: Double,
    val dosageUnit: String,
    val isTaken: Boolean,
    val reminderTime: Int,
    val medicationId: String,
    @TypeConverters(IntPairConverter::class) val intakeTime: Pair<Int, Int>,         //hour, minute
    @TypeConverters(IntPairConverter::class) val intakeDate: Pair<Int, Int>,         //day, month
    @TypeConverters(IntPairConverter::class) val actualIntakeTime: Pair<Int, Int>?,  //hour, minute
    @TypeConverters(IntPairConverter::class) val actualIntakeDate: Pair<Int, Int>?,  //day, month
    val intakeType: String,
) {
    class IntPairConverter {
        @TypeConverter
        fun fromIntPair(value: Pair<Int, Int>?): String? {
            return value?.let { "${it.first},${it.second}" }
        }

        @TypeConverter
        fun toIntPair(value: String?): Pair<Int, Int>? {
            return value?.split(",")?.let {
                Pair(it[0].toInt(), it[1].toInt())
            }
        }
    }
}
