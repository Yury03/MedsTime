package com.example.medstime.di

import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.common.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.common.ChangeNotificationStatus
import com.example.domain.usecase.medication.ChangeActualTimeIntake
import com.example.domain.usecase.medication.GetIntakeList
import com.example.domain.usecase.medication.GetMedicationById
import com.example.domain.usecase.medication.RemoveMedicationModel
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.domain.usecase.reminder.GetReminderModelById
import com.example.domain.usecase.reminder.GetRemindersWithStatus
import org.koin.dsl.module

val domainModule = module {
    /**Medication contract*/
    factory<GetIntakeList> {
        GetIntakeList(repository = get())
    }
    factory<ReplaceMedicationModel> {
        ReplaceMedicationModel(repository = get())
    }
    factory<RemoveMedicationModel> {
        RemoveMedicationModel(repository = get())
    }
    factory<GetMedicationById> {
        GetMedicationById(repository = get())
    }
    factory<ChangeActualTimeIntake> {
        ChangeActualTimeIntake(repository = get())
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