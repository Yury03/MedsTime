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
    private val _intakeList: MutableLiveData<List<MedicationIntakeModel>> =
        MutableLiveData(getIntakeList.invoke())
    val intakeList: LiveData<List<MedicationIntakeModel>> get() = _intakeList

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

    @RequiresApi(Build.VERSION_CODES.O)//TODO
    fun setCurrentDate() {
        //todo метод устанавливает фактическую дату, необходимо доработать до обновления в риалтайме
        val formatterIntakeDate = DateTimeFormatter.ofPattern("d M")
        val scanner = Scanner(LocalDateTime.now().format(formatterIntakeDate))
        val intakeDate = MedicationIntakeModel.Date(scanner.nextInt(), scanner.nextInt())

        getIntakeListWithDate(intakeDate)
    }

    /**метод устанавливает дату на верхней кнопке, по дефолту это текущая дата**/
    @RequiresApi(Build.VERSION_CODES.O)//TODO
    fun setDisplayDate(date: MedicationIntakeModel.Date) {
        val formatter = DateTimeFormatter.ofPattern(
            "d MMMM", Locale(
                "ru", "RU"
            )
        )
        val dateForTopButton = LocalDate.of(2023, date.month, date.day).format(formatter)
        _currentDate.postValue(dateForTopButton)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getIntakeListWithDate(date: MedicationIntakeModel.Date) {
        setDisplayDate(date)
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