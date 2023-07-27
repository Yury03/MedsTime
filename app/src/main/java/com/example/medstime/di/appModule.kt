package com.example.medstime.di

import com.example.medstime.ui.main_activity.MainViewModel
import com.example.medstime.ui.medication.MedicationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel()//todo usecase для добавления medicationModel и MedsTrackingModel
    }
    viewModel<MedicationViewModel> {
        MedicationViewModel(
            getMedicationListUseCase = get(),
            removeMedicationItemUseCase = get(),
            replaceMedicationItemUseCase = get()
        )
    }
}