package com.example.medstime.di

import com.example.data.MedicationContractImpl
import com.example.domain.Repository
import org.koin.dsl.module

val dataModule = module {
    single<Repository.MedicationContract> {
        MedicationContractImpl(context = get())
    }
}