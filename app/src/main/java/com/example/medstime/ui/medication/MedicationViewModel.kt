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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Scanner

class MedicationViewModel(
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

    /** фактическая дата: нужна для кнопки и начального displayDate**/
    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String>
        get() = _currentDate

    /**выбранная пользователем дата, по дефолту currentDate (фактическая)**/
    private val _displayDate = MutableLiveData<MedicationIntakeModel.Date>()
    val displayDate: LiveData<MedicationIntakeModel.Date>
        get() = _displayDate


    fun setCurrentDate() {
        //todo метод устанавливает фактическую дату, необходимо доработать до обновления в риалтайме
        val formatterIntakeDate = DateTimeFormatter.ofPattern("d M")
        val scanner = Scanner(LocalDateTime.now().format(formatterIntakeDate))
        val intakeDate = MedicationIntakeModel.Date(
            scanner.nextInt(), scanner.nextInt()
        )

        getIntakeListWithDate(intakeDate)
    }

    /**метод устанавливает дату на верхней кнопке, по дефолту это текущая дата**/

    private fun setDisplayDate(date: MedicationIntakeModel.Date) {
        val formatter = DateTimeFormatter.ofPattern(
            "d MMMM", Locale(
                "ru", "RU"
            )
        )
        val dateForTopButton = LocalDate.of(2023, date.month, date.day).format(formatter)
        _currentDate.postValue(dateForTopButton)
    }


    fun getIntakeListWithDate(date: MedicationIntakeModel.Date) {
        setDisplayDate(date)
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