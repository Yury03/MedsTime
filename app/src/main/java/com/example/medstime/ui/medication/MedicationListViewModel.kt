package com.example.medstime.ui.medication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class MedicationListViewModel(
    private val getIntakeListUseCase: GetIntakeList,
    private val removeMedicationModelUseCase: RemoveMedicationModel,
    private val changeMedicationIntakeIsTakenUseCase: ChangeMedicationIntakeIsTaken,
    private val changeNotificationStatusUseCase: ChangeNotificationStatusByReminderId,
    private val changeActualTimeIntakeUseCase: ChangeActualTimeIntake,
) : ViewModel() {
    /**Все существующие приемы, сортировка происходит внутри фрагмента*/
    lateinit var intakeListToday: LiveData<List<MedicationIntakeModel>>

    companion object {
        const val LOG_TAG = "MedicationListViewModel"
    }

    fun initIntakeListToday() {
        runBlocking {//TODO!!!!!
            intakeListToday = getIntakeListUseCase.invoke().asLiveData(Dispatchers.Main)
        }
        Log.d(LOG_TAG, intakeListToday.value.toString())
    }


    fun changeIsTakenStatus(medicationIntakeId: String, isTaken: Boolean) {
        val time = if (isTaken) {
            getActualTime()
        } else {
            null
        }
        viewModelScope.launch(Dispatchers.IO) {
            changeMedicationIntakeIsTakenUseCase.invoke(medicationIntakeId, isTaken, time)
            val newReminderStatus = if (isTaken) {
                ReminderModel.Status.TAKEN
            } else {
                ReminderModel.Status.SKIP
            }
            changeNotificationStatusUseCase.invoke(medicationIntakeId, newReminderStatus)
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
        removeMedicationModelUseCase.invoke(medicationModelId)
    }

    fun changeActualTime(medicationIntakeId: String, time: MedicationIntakeModel.Time) {
        viewModelScope.launch(Dispatchers.IO) {
            changeActualTimeIntakeUseCase.invoke(medicationIntakeId, time)
        }
    }
}