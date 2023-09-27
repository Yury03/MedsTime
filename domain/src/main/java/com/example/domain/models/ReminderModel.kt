package com.example.domain.models

data class ReminderModel(
    val id: String,
    val medicationIntakeId: String,
    val type: ReminderType,
    val status: ReminderStatus,
    val timeShow: Long,
) {
    enum class ReminderType {
        BANNER,
        PUSH_NOTIFICATION,
    }

    enum class ReminderStatus {
        SKIP,
        TAKEN,
        REMIND_LATER,
        NONE,
    }
}


