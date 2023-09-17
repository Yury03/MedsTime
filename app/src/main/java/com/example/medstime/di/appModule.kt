package com.example.medstime.di

import com.example.medstime.ui.add_med.AddMedViewModel
import com.example.medstime.ui.main_activity.MainViewModel
import com.example.medstime.ui.medication.MedicationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel()
    }
    viewModel<MedicationListViewModel> {
        MedicationListViewModel(
            getIntakeList = get(),
            removeMedicationItemUseCase = get(),
            replaceMedicationItemUseCase = get(),
            getMedicationById = get(),
            replaceMedicationIntake = get(),
        )
    }
    viewModel<AddMedViewModel> {
        AddMedViewModel(saveNewMedication = get())
    }
}