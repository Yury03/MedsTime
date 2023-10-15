package com.example.medstime.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.ReminderModel
import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.common.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.common.ChangeNotificationStatus
import com.example.domain.usecase.medication.ChangeActualTimeIntake
import com.example.domain.usecase.medication.GetIntakeList
import com.example.domain.usecase.medication.RemoveMedicationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class MedicationListViewModel(
    private val getIntakeListUseCase: GetIntakeList,
    private val removeMedicationModelUseCase: RemoveMedicationModel,
    private val changeMedicationIntakeIsTakenUseCase: ChangeMedicationIntakeIsTaken,
    private val changeNotificationStatusUseCase: ChangeNotificationStatus,
    private val changeActualTimeIntakeUseCase: ChangeActualTimeIntake,
    private val saveNewMedicationUseCase: SaveNewMedication,
) : ViewModel() {
    /**список пар Время - Список приемов лекарств**/
    private val _intakeListToday: MutableLiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>> =
        MutableLiveData()
    val intakeListToday: LiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>>
        get() = _intakeListToday
    private lateinit var _date: MedicationIntakeModel.Date

    fun setDate(date: MedicationIntakeModel.Date) {
        _date = date
    }

    fun getIntakeList() {
        viewModelScope.launch(Dispatchers.IO) {
            callGetIntakeList()
        }
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
            changeNotificationStatusUseCase.invoke(newReminderStatus, medicationIntakeId)
            callGetIntakeList()
        }
    }

    /****callGetIntakeList не использует асинхронный код** */
    private fun callGetIntakeList() {
        val intakeList = getIntakeListUseCase.invoke()
        val list = intakeList.filter {
            it.intakeDate == _date
        }
        //если в данную дату есть приемы, то делим их на пары типа Время - Список приемов
        val groupedMedications: List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>> =
            list.groupBy { it.intakeTime }.map { (time, medications) ->
                time to medications
            }
        val sortedList =
            groupedMedications.sortedWith(compareBy({ it.first.hour }, { it.first.minute }))
        _intakeListToday.postValue(sortedList)
    }

    private fun getActualTime(): MedicationIntakeModel.Time {
        with(Calendar.getInstance()) {
            return MedicationIntakeModel.Time(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
        }
    }

    fun removeMedicationModel(medicationModelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            callRemoveMedication(medicationModelId)
            callGetIntakeList()
        }
    }

    /****callRemoveMedication не использует асинхронный код** */
    private fun callRemoveMedication(medicationModelId: String) {
        removeMedicationModelUseCase.invoke(medicationModelId)
    }

    fun changeActualTime(medicationIntakeId: String, time: MedicationIntakeModel.Time) {
        viewModelScope.launch(Dispatchers.IO) {
            changeActualTimeIntakeUseCase.invoke(medicationIntakeId, time)
            callGetIntakeList()
        }
    }
}