package com.example.medstime.ui.medication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.usecase.common.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.common.ChangeNotificationStatus
import com.example.domain.usecase.medication.GetIntakeList
import com.example.domain.usecase.medication.GetMedicationById
import com.example.domain.usecase.medication.RemoveMedicationItem
import com.example.domain.usecase.medication.ReplaceMedicationIntake
import com.example.domain.usecase.medication.ReplaceMedicationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class MedicationListViewModel(
    private val getIntakeList: GetIntakeList,
    private val removeMedicationItemUseCase: RemoveMedicationItem,
    private val replaceMedicationItemUseCase: ReplaceMedicationItem,
    private val getMedicationById: GetMedicationById,
    private val replaceMedicationIntake: ReplaceMedicationIntake,
    private val changeMedicationIntakeIsTaken: ChangeMedicationIntakeIsTaken,
    private val changeNotificationStatus: ChangeNotificationStatus,
) : ViewModel() {
    /**список пар Время - Список приемов лекарств**/
    private val _intakeListToday: MutableLiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>> =
        MutableLiveData()
    val intakeListToday: LiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>>
        get() = _intakeListToday

    fun getIntakeListWithDate(date: MedicationIntakeModel.Date) {
        viewModelScope.launch(Dispatchers.IO) {
            val intakeList = getIntakeList.invoke()
            val list = intakeList.filter {
                it.intakeDate == date
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
    }

    fun changeIsTakenStatus(medicationIntakeId: String, isTaken: Boolean) {
        val time = if (isTaken) {
            getActualTime()
        } else {
            null
        }
        viewModelScope.launch(Dispatchers.IO) {
            changeMedicationIntakeIsTaken.invoke(medicationIntakeId, isTaken, time)
            Log.e("DEBUG", "OK")
        }
    }

    private fun getActualTime(): MedicationIntakeModel.Time {
        with(Calendar.getInstance()) {
            return MedicationIntakeModel.Time(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
        }
    }
}