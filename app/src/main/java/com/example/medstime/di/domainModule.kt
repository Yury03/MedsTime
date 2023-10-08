package com.example.medstime.di

import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.common.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.common.ChangeNotificationStatus
import com.example.domain.usecase.medication.GetIntakeList
import com.example.domain.usecase.medication.GetMedicationById
import com.example.domain.usecase.medication.RemoveMedicationItem
import com.example.domain.usecase.medication.ReplaceMedicationIntake
import com.example.domain.usecase.medication.ReplaceMedicationItem
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.domain.usecase.reminder.GetReminderModelById
import com.example.domain.usecase.reminder.GetRemindersWithStatus
import org.koin.dsl.module

val domainModule = module {
    /**Medication contract*/
    factory<GetIntakeList> {
        GetIntakeList(repository = get())
    }
    factory<ReplaceMedicationItem> {
        ReplaceMedicationItem(repository = get())
    }
    factory<RemoveMedicationItem> {
        RemoveMedicationItem(repository = get())
    }
    factory<GetMedicationById> {
        GetMedicationById(repository = get())
    }
    factory<ReplaceMedicationIntake> {
        ReplaceMedicationIntake(repository = get())
    }
    /**Additional contract*/
    factory<SaveNewMedication> {
        SaveNewMedication(repository = get())
    }
    /**Reminder contract*/
    factory<GetRemindersWithStatus> {
        GetRemindersWithStatus(repository = get())
    }
    factory<GetMedicationIntakeModel> {
        GetMedicationIntakeModel(repository = get())
    }
    factory<GetReminderModelById> {
        GetReminderModelById(repository = get())
    }
    /**Common contract*/
    factory<ChangeNotificationStatus> {
        ChangeNotificationStatus(repository = get())
    }
    factory<ChangeMedicationIntakeIsTaken> {
        ChangeMedicationIntakeIsTaken(repository = get())
    }
}