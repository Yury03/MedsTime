package com.example.medstime.ui.add_med

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.medication.GetMedicationById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddMedViewModel(
    private val saveNewMedicationUseCase: SaveNewMedication,
    private val getMedicationModelUseCase: GetMedicationById,
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
                if (event.mode == AddMedState.EDIT_MODE) {
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
            val numberOfDays = model.numberOfDays?.toString() ?: ""
            val stockOfMedicine = model.stockOfMedicine?.toString() ?: ""
            val endDate = model.endDate?.toString() ?: ""//todo date
            _state.postValue(
                state.value!!.copy(
                    medicationName = model.name,
                    dosage = model.dosage.toString(),//todo double
                    dosageUnits = model.dosageUnit,
                    startIntakeDate = model.startDate.toString(),//todo date
                    medComment = model.comment,
                    useBannerChBox = model.useBanner,
                    intakeTime = model.intakeTimes,
                    medicationReminderTime = model.reminderTime.toString(),
                    intakeType = model.intakeType.toString(),//todo test
                    frequency = model.frequency.toString(),//todo test
                    selectedDays = selectedDays,
                    trackType = model.trackType.toString(),//todo test
                    stockOfMedicine = stockOfMedicine,
                    numberOfDays = numberOfDays,
                    endDate = endDate,
                )
            )
        }
    }

    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return LocalDate.now().format(formatter)
    }
}