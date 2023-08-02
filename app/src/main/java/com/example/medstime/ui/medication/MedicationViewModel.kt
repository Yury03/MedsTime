package com.example.medstime.ui.medication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.domain.usecase.medication.GetIntakeList
import com.example.medstime.domain.usecase.medication.GetMedicationById
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationIntake
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Scanner

class MedicationViewModel(
    getIntakeList: GetIntakeList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem,
    getMedicationById: GetMedicationById,
    replaceMedicationIntake: ReplaceMedicationIntake,
) : ViewModel() {
    private val _intakeList: MutableLiveData<List<MedicationIntakeModel>> =
        MutableLiveData(getIntakeList.invoke())
    val intakeList: LiveData<List<MedicationIntakeModel>> get() = _intakeList

    private val _intakeListToday: MutableLiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>> =
        MutableLiveData()//список пар Время - Список приемов лекарств
    val intakeListToday: LiveData<List<Pair<MedicationIntakeModel.Time, List<MedicationIntakeModel>>>>
        get() = _intakeListToday
    private val _currentDate = MutableLiveData<Pair<String, MedicationIntakeModel.Date>>()
    val currentDate: LiveData<Pair<String, MedicationIntakeModel.Date>>
        get() = _currentDate


    @RequiresApi(Build.VERSION_CODES.O)//TODO
    fun setDate() {//todo необходимо обновление даты и времени в риалтайме
        val formatterTopButton = DateTimeFormatter.ofPattern(
            "d MMMM", Locale(
                "ru", "RU"
            )
        )//получаем дату в двух форматах: String для верхней кнопки фрагмента,
        // Date для обработки внутри ViewModel и Fragment
        val formatterIntakeDate = DateTimeFormatter.ofPattern("d M")
        val dateForTopButton = LocalDateTime.now().format(formatterTopButton)
//        val scanner = Scanner(LocalDateTime.now().format(formatterIntakeDate))
        val scanner = Scanner("19 7")//placeholder
        val intakeDate = MedicationIntakeModel.Date(scanner.nextInt(), scanner.nextInt())
        _currentDate.postValue(Pair(dateForTopButton, intakeDate))
        getIntakeListWithDate(intakeDate)
    }

    private fun getIntakeListWithDate(date: MedicationIntakeModel.Date) {
        val list = _intakeList.value?.filter {
            it.intakeDate == date
        }
        //если в данную дату есть приемы, то делим их на пары типа Время - Список приемов
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