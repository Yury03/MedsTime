package com.example.medstime.di

import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.domain.usecase.reminder.GetRemindersWithStatus
import com.example.medstime.ui.add_med.AddMedViewModel
import com.example.medstime.ui.main_activity.MainViewModel
import com.example.medstime.ui.medication.MedicationListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel()
    }
    viewModel<MedicationListViewModel> {
        MedicationListViewModel(
            getIntakeListUseCase = get(),
            removeMedicationModelUseCase = get(),
            getMedicationByIdUseCase = get(),
            changeMedicationIntakeIsTakenUseCase = get(),
            changeNotificationStatusUseCase = get(),
            changeActualTimeIntakeUseCase = get(),
            saveNewMedicationUseCase = get(),
        )
    }
    viewModel<AddMedViewModel> {
        AddMedViewModel(saveNewMedication = get())
    }
    single<GetRemindersWithStatus> {
        GetRemindersWithStatus(get())
    }
    single<GetMedicationIntakeModel> {
        GetMedicationIntakeModel(get())
    }
    single { Dispatchers.IO }
}