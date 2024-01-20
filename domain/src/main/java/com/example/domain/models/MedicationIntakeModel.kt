package com.example.domain.models

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