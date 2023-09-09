package com.example.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "medication_database")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,//todo ColumnInfo?
    val dosage: Double,
    val dosageUnit: String,
    @TypeConverters(IntPairListConverter::class) val intakeTimes: List<Pair<Int, Int>>,
    val reminderTime: Int,
    val frequency: String,
    @TypeConverters(IntListConverter::class) val selectedDays: List<Int>?,
    val intakeType: String,
    val startDate: String,
    val endDate: String?,
) {
    class IntListConverter {
        @TypeConverter
        fun fromIntList(value: List<Int>): String {
            return value.joinToString(",")
        }

        @TypeConverter
        fun toIntList(value: String?): List<Int>? {
            return value?.split(",")?.mapNotNull { it.toIntOrNull() }
        }
    }


    class IntPairListConverter {
        @TypeConverter
        fun fromIntPairList(value: List<Pair<Int, Int>>): String {
            return value.joinToString(";") { "${it.first},${it.second}" }
        }

        @TypeConverter
        fun toIntPairList(value: String): List<Pair<Int, Int>> {
            return value.split(";").map {
                val parts = it.split(",")
                Pair(parts[0].toInt(), parts[1].toInt())
            }
        }
    }

}




