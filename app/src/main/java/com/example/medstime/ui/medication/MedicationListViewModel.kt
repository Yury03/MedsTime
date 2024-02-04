package com.example.medstime.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.ReminderModel
import com.example.domain.usecase.common.RemoveMedicationModel
import com.example.domain.usecase.medication_intake.ChangeActualTimeIntake
import com.example.domain.usecase.medication_intake.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.medication_intake.GetIntakeList
import com.example.domain.usecase.reminder.ChangeNotificationStatusByReminderId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class MedicationListViewModel(
    private val getIntakeListUseCase: GetIntakeList,
    private val removeMedicationModelUseCase: RemoveMedicationModel,
    private val changeMedicationIntakeIsTakenUseCase: ChangeMedicationIntakeIsTaken,
    private val changeNotificationStatusUseCase: ChangeNotificationStatusByReminderId,
    private val changeActualTimeIntakeUseCase: ChangeActualTimeIntake,
) : ViewModel() {

    /**Все существующие приемы, сортировка происходит внутри фрагмента*/
    private val _intakeListToday = MutableLiveData<List<MedicationIntakeModel>>()
    val intakeListToday: LiveData<List<MedicationIntakeModel>> get() = _intakeListToday

    init {
        viewModelScope.launch {
            getIntakeListUseCase()
                .collect { result ->
                    _intakeListToday.value = result
                }
        }
    }


    fun changeIsTakenStatus(medicationIntakeId: String, isTaken: Boolean) {
        val time = if (isTaken) {
            getActualTime()
        } else {
            null
        }
        viewModelScope.launch(Dispatchers.IO) {
            changeMedicationIntakeIsTakenUseCase(medicationIntakeId, isTaken, time)
            val newReminderStatus = if (isTaken) {
                ReminderModel.Status.TAKEN
            } else {
                ReminderModel.Status.SKIP
            }
            changeNotificationStatusUseCase(medicationIntakeId, newReminderStatus)
        }
    }

    private fun getActualTime(): MedicationIntakeModel.Time {
        with(Calendar.getInstance()) {
            return MedicationIntakeModel.Time(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
        }
    }

    fun removeMedicationModel(medicationModelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            callRemoveMedication(medicationModelId)
        }
    }

    private suspend fun callRemoveMedication(medicationModelId: String) {
        removeMedicationModelUseCase(medicationModelId)
    }

    fun changeActualTime(medicationIntakeId: String, time: MedicationIntakeModel.Time) {
        viewModelScope.launch(Dispatchers.IO) {
            changeActualTimeIntakeUseCase(medicationIntakeId, time)
        }
    }

    companion object {

        const val LOG_TAG = "MedicationListViewModel"
    }
}