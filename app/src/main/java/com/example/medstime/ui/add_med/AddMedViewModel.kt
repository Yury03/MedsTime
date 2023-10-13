package com.example.medstime.ui.add_med

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationModel
import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.medication.GetMedicationById
import com.example.medstime.R
import com.example.medstime.ui.add_med.AddMedState.Companion.EDIT_MODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class AddMedViewModel(
    private val saveNewMedicationUseCase: SaveNewMedication,
    private val getMedicationModelUseCase: GetMedicationById,
    private val resources: Resources,
) : ViewModel() {
    private val _state: MutableLiveData<AddMedState> =
        MutableLiveData()
    val state: LiveData<AddMedState>
        get() = _state

    init {
        _state.value = AddMedState(
            mode = AddMedState.ADD_MODE,
            isSavedNewMedication = false,
            medicationName = "",
            dosage = "",
            dosageUnits = "",
            startIntakeDate = getCurrentDate(),
            medComment = "",
            useBannerChBox = false,
            intakeTime = listOf(),
            medicationReminderTime = "",
            intakeType = "",
            frequency = "",
            selectedDays = listOf(),
            trackType = "",
            stockOfMedicine = "",
            numberOfDays = "",
            endDate = ""
        )
    }

    fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

    fun load() {
        runBlocking {//TODO!!!
            viewModelScope.launch(Dispatchers.IO) {
//                saveNewMedicationUseCase.invoke()

//                _state.postValue(
//
//                )
            }
        }
    }

    fun send(event: AddMedEvent) {
        when (event) {
            is AddMedEvent.Mode -> {
                if (event.mode == EDIT_MODE) {

                    getMedicationModelById(event.medicationModelId)
                }
            }

            is AddMedEvent.MedicationModelSaved -> {
                _state.value = _state.value!!.copy(isSavedNewMedication = event.isSaved)
            }

            is AddMedEvent.MedicationNameChanged -> {
                _state.value = _state.value!!.copy(medicationName = event.medicationName)
            }

            is AddMedEvent.DosageChanged -> {
                _state.value = _state.value!!.copy(dosage = event.newDosage)
            }

            is AddMedEvent.DosageUnitsChanged -> {
                _state.value = _state.value!!.copy(dosageUnits = event.newDosageUnits)
            }

            is AddMedEvent.EndDateChanged -> {
                _state.value = _state.value!!.copy(endDate = event.newEndDate)
            }

            is AddMedEvent.FrequencyChanged -> {
                _state.value = _state.value!!.copy(frequency = event.newFrequency)
            }

            is AddMedEvent.IntakeTimeChanged -> {
                _state.value = _state.value!!.copy(intakeTime = event.newIntakeTime)
            }

            is AddMedEvent.MedCommentChanged -> {
                _state.value = _state.value!!.copy(medComment = event.newMedComment)
            }

            is AddMedEvent.MedicationReminderTimeChanged -> {
                _state.value = _state.value!!.copy(medicationReminderTime = event.newReminderTime)
            }

            is AddMedEvent.NumberOfDaysChanged -> {
                _state.value = _state.value!!.copy(numberOfDays = event.newNumberOfDays)
            }

            is AddMedEvent.SelectedDaysChanged -> {
                _state.value = _state.value!!.copy(selectedDays = event.newSelectedDays)
            }

            is AddMedEvent.StartIntakeDateChanged -> {
                _state.value = _state.value!!.copy(startIntakeDate = event.newStartIntakeDate)
            }

            is AddMedEvent.StockOfMedicineChanged -> {
                _state.value = _state.value!!.copy(stockOfMedicine = event.newStock)
            }

            is AddMedEvent.TrackTypeChanged -> {
                _state.value = _state.value!!.copy(trackType = event.newTrackType)
            }

            is AddMedEvent.UseBannerCheckBoxChanged -> {
                _state.value = _state.value!!.copy(useBannerChBox = event.newUseBanner)
            }
        }

    }


    private fun getMedicationModelById(medicationModelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = getMedicationModelUseCase.invoke(medicationModelId)
            val selectedDays = model.selectedDays ?: listOf()
            val numberOfDays = model.numberOfDays?.toDisplayString() ?: ""
            val stockOfMedicine = model.stockOfMedicine?.toString() ?: ""
            val endDate = model.endDate?.toDisplayString() ?: ""
            _state.postValue(
                state.value!!.copy(
                    medicationName = model.name,
                    dosage = model.dosage.toDisplayString(),
                    dosageUnits = model.dosageUnit,
                    startIntakeDate = model.startDate.toDisplayString(),
                    medComment = model.comment,
                    useBannerChBox = model.useBanner,
                    intakeTime = model.intakeTimes,
                    medicationReminderTime = model.reminderTime.toAdapterString(),
                    intakeType = model.intakeType.toAdapterString(),
                    frequency = model.frequency.toAdapterString(),
                    selectedDays = selectedDays,
                    trackType = model.trackType.toAdapterString(),
                    stockOfMedicine = stockOfMedicine,
                    numberOfDays = numberOfDays,
                    endDate = endDate,
                    mode = EDIT_MODE
                )
            )
        }
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return LocalDate.now().format(formatter)
    }

    /**### Функция *Double.toDisplayString()* убирает незначащие нули и возвращает строку готовую для отображения*/
    private fun Double.toDisplayString(): String {
        return if (this == this.toInt().toDouble()) {
            this.toInt().toString()
        } else {
            this.toString()
        }
    }

    /**### Функция *Date.toDisplayString()* приводит java.util.Date к строке вида dd.MM.yyyy*/
    private fun Date.toDisplayString(): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(this)
    }

    /**### Функция *MedicationModel.IntakeType.toAdapterString()* переводит данные в строку из адаптера AutoCompleteTextView*/
    private fun MedicationModel.IntakeType.toAdapterString(): String {
        val intakeTypeArray = resources.getStringArray(R.array.intake_array)
        return when (this) {
            MedicationModel.IntakeType.NONE -> intakeTypeArray[0]
            MedicationModel.IntakeType.BEFORE_MEAL -> intakeTypeArray[1]
            MedicationModel.IntakeType.DURING_MEAL -> intakeTypeArray[2]
            MedicationModel.IntakeType.AFTER_MEAL -> intakeTypeArray[3]
        }
    }

    /**### Функция *Int.toAdapterString* переводит данные **REMINDER TYPE** в строку из адаптера AutoCompleteTextView*/
    private fun Int.toAdapterString(): String {
        val reminderTypeArray = resources.getStringArray(R.array.reminder_array)
        return when (this) {
            0 -> reminderTypeArray[0]
            5 -> reminderTypeArray[1]
            10 -> reminderTypeArray[2]
            15 -> reminderTypeArray[3]
            20 -> reminderTypeArray[4]

            else -> {
                //обработка ошибки
                reminderTypeArray[0]
            }
        }
    }

    /**### Функция *MedicationModel.TrackType.toAdapterString()* переводит данные в строку из адаптера AutoCompleteTextView*/
    private fun MedicationModel.TrackType.toAdapterString(): String {
        val trackTypeArray = resources.getStringArray(R.array.track_array)
        return when (this) {
            MedicationModel.TrackType.NONE -> trackTypeArray[0]
            MedicationModel.TrackType.STOCK_OF_MEDICINE -> trackTypeArray[1]
            MedicationModel.TrackType.NUMBER_OF_DAYS -> trackTypeArray[2]
            MedicationModel.TrackType.DATE -> trackTypeArray[3]
        }
    }

    /**### Функция *MedicationModel.Frequency.toAdapterString()* переводит данные в строку из адаптера AutoCompleteTextView*/
    private fun MedicationModel.Frequency.toAdapterString(): String {
        val frequencyTypeArray = resources.getStringArray(R.array.frequency_array)
        return when (this) {
            MedicationModel.Frequency.SELECTED_DAYS -> frequencyTypeArray[0]
            MedicationModel.Frequency.EVERY_OTHER_DAY -> frequencyTypeArray[1]
            MedicationModel.Frequency.DAILY -> frequencyTypeArray[2]
        }
    }
}





