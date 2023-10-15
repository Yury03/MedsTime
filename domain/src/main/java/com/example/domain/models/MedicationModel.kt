package com.example.domain.models

import java.util.Date

data class MedicationModel(
    val id: String,
    val name: String, // Название лекарства (например, "Депакин")
    val dosage: Double, // Разовая доза (например, 0.5)
    val dosageUnit: String, // Единица измерения дозы (например, "таблетка")
    val intakeTimes: List<Time>, // Время приема лекарства в течение дня
    val reminderTime: Int, // Время за которое напоминать (в минутах)
    val frequency: Frequency, // Частота приема лекарства (например, "DAILY", "EVERY_OTHER_DAY", "SELECTED_DAYS")
    val selectedDays: List<Int>?, // Список выбранных дней приема (если применимо)
    val startDate: Date,// Дата начала приема
    val intakeType: IntakeType,// Тип приема
    val comment: String,//Комментарий
    val useBanner: Boolean,//Использовать баннер
    val trackType: TrackType,
    val stockOfMedicine: Double?,
    val endDate: Date?,
    val numberOfDays: Double?,
) {
    data class Time(
        val hour: Int, // Час
        val minute: Int // Минуты
    )

    enum class Frequency {
        DAILY, // Прием каждый день
        EVERY_OTHER_DAY, // Прием через день
        SELECTED_DAYS, // Прием в выбранные дни;;
    }

    enum class IntakeType {
        AFTER_MEAL,    // После еды
        BEFORE_MEAL,   // До еды
        DURING_MEAL,   // Во время еды
        NONE,          // Без еды (не указано)
    }

    enum class TrackType {
        STOCK_OF_MEDICINE,  // Запас лекарств
        DATE,               // Дата
        NUMBER_OF_DAYS,     // Количество дней
        NONE,               // Не добавлять отслеживание
    }
}




