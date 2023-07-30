package com.example.medstime.ui.di

import com.example.medstime.domain.usecase.medication.GetIntakeList
import com.example.medstime.domain.usecase.medication.GetMedicationById
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem
import org.koin.dsl.module

val domainModule = module {
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
}