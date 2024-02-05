package com.example.domain.models

import kotlinx.serialization.Serializable

data class MedicationIntakeModel(
    val id: String,
    val name: String,                   // Название лекарства
    val dosage: Double,                 // Разовая доза
    val dosageUnit: String,             // Единица измерения дозы (например, "таблетка")
    val isTaken: Boolean = true,        // Факт приема (реальный факт приема определяется условием: isTaken && actualIntakeTime != null)
    val reminderTime: Int,              // Время за которое напоминать (в минутах)
    val medicationId: String,           // id модели приема
    val intakeTime: Time,               // Время приема лекарства
    val intakeDate: Date,               // Дата приема лекарства
    val actualIntakeTime: Time? = null, // Время фактического приема лекарства
    val actualIntakeDate: Date? = null, // Дата фактического приема лекарства
    val intakeType: IntakeType,         // Тип приема
) {

    data class Time(
        val hour: Int,  // Час
        val minute: Int,// Минуты
    ) {

        fun toEntityString(): String {
            return "${hour},${minute}"
        }

        fun toDisplayString(): String {
            return if (minute < 10) {
                "${hour}:0${minute}"
            } else {
                "${hour}:${minute}"
            }
        }
    }

    /*todo WARNING! kotlinx.serialization compiler plugin is not applied to the module,
       so this annotation would not be processed. Make sure that you've setup
       your buildscript correctly and re-import project.*/
    @Serializable
    data class Date(
        val day: Int = 0,   // День
        val month: Int = 0, // Месяц
        val year: Int = 0,  // Год
    )

    enum class IntakeType {
        AFTER_MEAL,    // После еды
        BEFORE_MEAL,   // До еды
        DURING_MEAL,   // Во время еды
        NONE           // Без еды (не указано)
    }
}
