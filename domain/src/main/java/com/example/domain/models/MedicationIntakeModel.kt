package com.example.domain.models

data class MedicationIntakeModel(
    val id: String,
    val name: String,               // Название лекарства (например, "Депакин")
    val dosage: Double,             // Разовая доза (например, 0.5)
    val dosageUnit: String,         // Единица измерения дозы (например, "таблетка")
    val isTaken: Boolean,           // Факт приема (принято или не принято)
    val reminderTime: Int,          // Время за которое напоминать (в минутах)
    val medicationId: Int,          // id модели приема
    val intakeTime: Time,           // Время приема лекарства
    val intakeDate: Date,           // Дата приема лекарства
    val actualIntakeTime: Time?,    // Время фактического приема лекарства
    val actualIntakeDate: Date?,    // Дата фактического приема лекарства
    val intakeType: IntakeType,     // Тип приема
) {
    data class Time(
        val hour: Int,  // Час
        val minute: Int,// Минуты
    )

    data class Date(
        val day: Int,   // День
        val month: Int, // Месяц
    )

    enum class IntakeType {
        AFTER_MEAL,    // После еды
        BEFORE_MEAL,   // До еды
        DURING_MEAL,   // Во время еды
        NONE           // Без еды (не указано)
    }
}