package com.example.medstime.ui.di

import com.example.medstime.data.MedicationContractImpl
import org.koin.dsl.module

val dataModule = module {
    single<MedicationContractImpl> {
        MedicationContractImpl(context = get())
    }
}