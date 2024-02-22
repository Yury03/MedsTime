package com.example.domain.models

data class MedicationModel(
    val id: String,
    val name: String, // Название лекарства
    val dosage: Double, // Разовая доза
    val dosageUnit: String, // Единица измерения дозы
    val intakeTimes: List<Time>, // Время приема лекарства в течение дня
    val reminderTime: Int, // Время за которое напоминать (в минутах)
    val frequency: Frequency, // Частота приема лекарства
    val selectedDays: List<Int>?, // Список выбранных дней приема [для SELECTED_DAYS]
    val startDate: Long,// Дата начала приема
    val intakeType: IntakeType,// Тип приема
    val comment: String,//Комментарий
    val useBanner: Boolean,//Использовать баннер
    var trackModel: MedsTrackModel = MedsTrackModel(),
) {

    data class Time(
        val hour: Int, // Час
        val minute: Int // Минуты
    )

    enum class Frequency {
        DAILY, // Прием каждый день
        EVERY_OTHER_DAY, // Прием через день
        SELECTED_DAYS, // Прием в выбранные дни
    }

    enum class IntakeType {
        AFTER_MEAL,    // После еды
        BEFORE_MEAL,   // До еды
        DURING_MEAL,   // Во время еды
        NONE,          // Без еды (не указано)
    }
}