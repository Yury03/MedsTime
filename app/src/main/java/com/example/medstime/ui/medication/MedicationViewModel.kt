package com.example.medstime.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    getIntakeList: GetIntakeList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem,
    getMedicationById: GetMedicationById,
    replaceMedicationIntake: ReplaceMedicationIntake,
) : ViewModel() {
    private val _intakeList: MutableLiveData<List<com.example.domain.models.MedicationIntakeModel>> =
        MutableLiveData()
    val intakeList: LiveData<List<com.example.domain.models.MedicationIntakeModel>> get() = _intakeList

    init {
        viewModelScope.launch(Dispatchers.IO) { getIntakeList.invoke() }
    }


    /**список пар Время - Список приемов лекарств**/
    private val _intakeListToday: MutableLiveData<List<Pair<com.example.domain.models.MedicationIntakeModel.Time, List<com.example.domain.models.MedicationIntakeModel>>>> =
        MutableLiveData()
    val intakeListToday: LiveData<List<Pair<com.example.domain.models.MedicationIntakeModel.Time, List<com.example.domain.models.MedicationIntakeModel>>>>
        get() = _intakeListToday

    /** фактическая дата: нужна для кнопки и начального displayDate**/
    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String>
        get() = _currentDate

    /**выбранная пользователем дата, по дефолту currentDate (фактическая)**/
    private val _displayDate =
        MutableLiveData<com.example.domain.models.MedicationIntakeModel.Date>()
    val displayDate: LiveData<com.example.domain.models.MedicationIntakeModel.Date>
        get() = _displayDate


    fun setCurrentDate() {
        //todo метод устанавливает фактическую дату, необходимо доработать до обновления в риалтайме
        val formatterIntakeDate = DateTimeFormatter.ofPattern("d M")
        val scanner = Scanner(LocalDateTime.now().format(formatterIntakeDate))
        val intakeDate = com.example.domain.models.MedicationIntakeModel.Date(
            scanner.nextInt(),
            scanner.nextInt()
        )

        getIntakeListWithDate(intakeDate)
    }

    /**метод устанавливает дату на верхней кнопке, по дефолту это текущая дата**/

    private fun setDisplayDate(date: com.example.domain.models.MedicationIntakeModel.Date) {
        val formatter = DateTimeFormatter.ofPattern(
            "d MMMM", Locale(
                "ru", "RU"
            )
        )
        val dateForTopButton = LocalDate.of(2023, date.month, date.day).format(formatter)
        _currentDate.postValue(dateForTopButton)
    }


    fun getIntakeListWithDate(date: com.example.domain.models.MedicationIntakeModel.Date) {
        setDisplayDate(date)
        val list = _intakeList.value?.filter {
            it.intakeDate == date
        }
        //если в данную дату есть приемы, то делим их на пары типа Время - Список приемов
        list?.let { notNullList ->
            val groupedMedications: List<Pair<com.example.domain.models.MedicationIntakeModel.Time, List<com.example.domain.models.MedicationIntakeModel>>> =
                notNullList.groupBy { it.intakeTime }
                    .map { (time, medications) ->
                        time to medications
                    }
            _intakeListToday.postValue(groupedMedications)
        }

    }
}