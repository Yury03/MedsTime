package com.example.medstime.di

import com.example.medstime.data.MedicationContractImpl
import com.example.medstime.domain.Repository
import org.koin.dsl.module

val dataModule = module {
    single<Repository.MedicationContract> {
        MedicationContractImpl(context = get())
    }
}