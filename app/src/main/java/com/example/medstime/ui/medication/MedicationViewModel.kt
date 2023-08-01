package com.example.medstime.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.domain.models.MedicationModel
import com.example.medstime.domain.usecase.medication.GetIntakeList
import com.example.medstime.domain.usecase.medication.GetMedicationById
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationIntake
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem

class MedicationViewModel(
    getIntakeList: GetIntakeList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem,
    getMedicationById: GetMedicationById,
    replaceMedicationIntake: ReplaceMedicationIntake,
) : ViewModel() {
    private val _intakeList: MutableLiveData<List<MedicationIntakeModel>> =
        MutableLiveData(/*getIntakeList.invoke()*/)
    val intakeList: LiveData<List<MedicationIntakeModel>> get() = _intakeList

    private val _intakeListToday:
            MutableLiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>> =
        MutableLiveData()
    val intakeListToday:
            LiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>>
        get() = _intakeListToday


    private fun removeMedication(medicationModel: MedicationModel) {

    }


    fun getIntakeListWithDate(date: MedicationIntakeModel.Date) {
        val list = _intakeList.value?.filter {
            it.intakeDate == date
        }
        list?.let { notNullList ->
            val groupedMedications: List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>> =
                notNullList.groupBy { it.intakeTime }
                    .map { (time, medications) ->
                        time to medications
                    }
            _intakeListToday.postValue(groupedMedications)
        }
    }


}