package com.example.medstime.di

import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.medication.GetIntakeList
import com.example.domain.usecase.medication.GetMedicationById
import com.example.domain.usecase.medication.RemoveMedicationItem
import com.example.domain.usecase.medication.ReplaceMedicationIntake
import com.example.domain.usecase.medication.ReplaceMedicationItem
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
}