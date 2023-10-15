package com.example.medstime.di

import com.example.data.CommonContractImpl
import com.example.data.MedicationContractImpl
import com.example.data.ReminderContractImpl
import com.example.domain.Repository
import org.koin.dsl.module

val dataModule = module {
    single<Repository.MedicationContract> {
        MedicationContractImpl(context = get())
    }
    single<Repository.ReminderContract> {
        ReminderContractImpl(context = get())
    }
    single<Repository.CommonContract> {
        CommonContractImpl(context = get())
    }
}