package com.example.domain.models

data class ReminderModel(
    val id: String,
    val medicationIntakeId: String,
    val type: Type,
    val status: Status,
    val timeShow: Long,
) {
    enum class Type {
        BANNER,
        PUSH_NOTIFICATION,
    }

    enum class Status {
        SKIP,
        TAKEN,
        REMIND_LATER,
        NONE,
        SHOWN,
    }
}


