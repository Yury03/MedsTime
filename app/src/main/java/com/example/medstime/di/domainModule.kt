package com.example.medstime.di

import com.example.medstime.domain.usecase.medication.GetMedicationsList
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem
import org.koin.dsl.module

val domainModule = module {
    factory<GetMedicationsList> {
        GetMedicationsList(repository = get())
    }
    factory<ReplaceMedicationItem> {
        ReplaceMedicationItem(repository = get())
    }
    factory<RemoveMedicationItem> {
        RemoveMedicationItem(repository = get())
    }
}