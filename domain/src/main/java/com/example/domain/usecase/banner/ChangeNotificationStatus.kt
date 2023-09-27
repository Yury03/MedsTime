package com.example.domain.usecase.banner

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class ChangeNotificationStatus(private val repository: Repository.BannerContract) {
    fun invoke(status: ReminderModel.ReminderStatus) {
        repository.changeNotificationStatus(status)
    }
}