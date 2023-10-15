package com.example.medstime.di

import android.content.res.Resources
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.domain.usecase.reminder.GetRemindersWithStatus
import com.example.medstime.ui.add_med.AddMedViewModel
import com.example.medstime.ui.main_activity.MainViewModel
import com.example.medstime.ui.medication.MedicationListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
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
            changeMedicationIntakeIsTakenUseCase = get(),
            changeNotificationStatusUseCase = get(),
            changeActualTimeIntakeUseCase = get(),
            saveNewMedicationUseCase = get(),
        )
    }
    viewModel<AddMedViewModel> {
        AddMedViewModel(
            saveNewMedicationUseCase = get(),
            getMedicationModelUseCase = get(),
            resources = get()
        )
    }
    single<Resources> { androidContext().resources }
    single<GetRemindersWithStatus> {
        GetRemindersWithStatus(get())
    }
    single<GetMedicationIntakeModel> {
        GetMedicationIntakeModel(get())
    }
    single { Dispatchers.IO }
}