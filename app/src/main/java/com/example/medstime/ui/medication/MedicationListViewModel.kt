package com.example.medstime.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.usecase.medication.GetIntakeList
import com.example.domain.usecase.medication.GetMedicationById
import com.example.domain.usecase.medication.RemoveMedicationItem
import com.example.domain.usecase.medication.ReplaceMedicationIntake
import com.example.domain.usecase.medication.ReplaceMedicationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationListViewModel(
    private val getIntakeList: GetIntakeList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem,
    getMedicationById: GetMedicationById,
    replaceMedicationIntake: ReplaceMedicationIntake,
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
}