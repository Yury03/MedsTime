package com.example.medstime.di

import com.example.domain.usecase.common.GetMedicationById
import com.example.domain.usecase.common.RemoveMedicationModel
import com.example.domain.usecase.common.ReplaceMedicationModel
import com.example.domain.usecase.common.SaveNewMedication
import com.example.domain.usecase.medication_intake.ChangeActualTimeIntake
import com.example.domain.usecase.medication_intake.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.medication_intake.GetIntakeList
import com.example.domain.usecase.medication_intake.GetMedicationIntakeModel
import com.example.domain.usecase.meds_track.GetAllTracks
import com.example.domain.usecase.meds_track.GetTrackById
import com.example.domain.usecase.reminder.ChangeNotificationStatusByMedIntakeId
import com.example.domain.usecase.reminder.ChangeNotificationStatusByReminderId
import com.example.domain.usecase.reminder.GetReminderModelById
import com.example.domain.usecase.reminder.GetRemindersWithStatus
import org.koin.dsl.module

val domainModule = module {
    /**Medication intake contract*/
    factory<ChangeActualTimeIntake> {
        ChangeActualTimeIntake(repository = get())
    }
    factory<ChangeMedicationIntakeIsTaken> {
        ChangeMedicationIntakeIsTaken(repository = get())
    }
    factory<GetIntakeList> {
        GetIntakeList(repository = get())
    }
    factory<GetMedicationIntakeModel> {
        GetMedicationIntakeModel(repository = get())
    }
    /**Reminder contract*/
    factory<ChangeNotificationStatusByReminderId> {
        ChangeNotificationStatusByReminderId(repository = get())
    }
    factory<ChangeNotificationStatusByMedIntakeId> {
        ChangeNotificationStatusByMedIntakeId(repository = get())
    }
    factory<GetReminderModelById> {
        GetReminderModelById(repository = get())
    }
    factory<GetRemindersWithStatus> {
        GetRemindersWithStatus(repository = get())
    }
    /**Meds track contract*/
    factory<GetAllTracks> {
        GetAllTracks(repository = get())
    }
    factory<GetTrackById> {
        GetTrackById(repository = get())
    }
    /**Common contract*/
    factory<GetMedicationById> {
        GetMedicationById(repository = get())
    }
    factory<RemoveMedicationModel> {
        RemoveMedicationModel(repository = get())
    }
    factory<ReplaceMedicationModel> {
        ReplaceMedicationModel(repository = get())
    }
    factory<SaveNewMedication> {
        SaveNewMedication(repository = get())
    }
}