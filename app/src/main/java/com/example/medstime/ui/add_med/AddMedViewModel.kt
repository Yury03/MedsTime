package com.example.medstime.ui.add_med

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationModel
import com.example.domain.usecase.addition.SaveNewMedication
import com.example.domain.usecase.common.ReplaceMedicationModel
import com.example.domain.usecase.medication.GetMedicationById
import com.example.medstime.R
import com.example.medstime.ui.add_med.AddMedState.Companion.ADD_MODE
import com.example.medstime.ui.add_med.AddMedState.Companion.EDIT_MODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID

class AddMedViewModel(
    private val saveNewMedicationUseCase: SaveNewMedication,
    private val getMedicationModelUseCase: GetMedicationById,
    private val replaceMedicationModelUseCase: ReplaceMedicationModel,
    private val resources: Resources,
) : ViewModel() {
    private val _state: MutableLiveData<AddMedState> =
        MutableLiveData()
    val state: LiveData<AddMedState>
        get() = _state
    var editMedicationModelId: String? = null

    init {
        _state.value = AddMedState(
            mode = ADD_MODE,
            isSavedNewMedication = false,
            inputError = 0,
            medicationName = "",
            dosage = "",
            dosageUnits = "",
            startIntakeDate = getCurrentDate(),
            medComment = "",
            useBannerChBox = false,
            intakeTimeList = listOf(),
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

    private fun saveNewModelInRoom(medicationModel: MedicationModel) {
        runBlocking {//TODO!!!
            viewModelScope.launch(Dispatchers.IO) {
                saveNewMedicationUseCase.invoke(medicationModel)//todo return true or false
                _state.postValue(state.value!!.copy(isSavedNewMedication = true))
            }
        }
    }

    fun send(event: AddMedEvent) {
        when (event) {
            AddMedEvent.ContinueButtonClicked -> {
                saveMedicationModel()
            }

            AddMedEvent.ErrorWasShown -> {
                _state.value = _state.value!!.copy(inputError = 0)
            }

            is AddMedEvent.Mode -> {
                if (event.mode == EDIT_MODE) {
                    editMedicationModelId = event.medicationModelId
                    getMedicationModelById(event.medicationModelId)
                }
            }

            is AddMedEvent.MedicationModelChanged -> {
                _state.value = _state.value!!.copy(
                    medicationName = event.medicationName,
                    dosage = event.newDosage,
                    dosageUnits = event.newDosageUnits,
                    endDate = event.newEndDate,
                    frequency = event.newFrequency,
                    intakeTimeList = event.newIntakeTime,
                    medComment = event.newMedComment,
                    medicationReminderTime = event.newReminderTime,
                    numberOfDays = event.newNumberOfDays,
                    selectedDays = event.newSelectedDays,
                    startIntakeDate = event.newStartIntakeDate,
                    stockOfMedicine = event.newStock,
                    trackType = event.newTrackType,
                    useBannerChBox = event.newUseBanner,
                )
            }
        }
    }

    private fun saveMedicationModel() {
        val medicationModel = makeMedicationModel()
        medicationModel.first?.let { model ->
            when (state.value!!.mode) {
                ADD_MODE -> saveNewModelInRoom(model)
                EDIT_MODE -> replaceModelInRoom(model)
            }

        } ?: _state.postValue(state.value!!.copy(inputError = medicationModel.second))
    }

    private fun replaceModelInRoom(medicationModel: MedicationModel) {
        runBlocking {//TODO!!!
            viewModelScope.launch(Dispatchers.IO) {
                replaceMedicationModelUseCase.invoke(medicationModel)//todo return true or false
                _state.postValue(state.value!!.copy(isSavedNewMedication = true))
            }
        }
    }

    private fun makeMedicationModel()
            : Pair<MedicationModel?, Int> {
        with(state.value!!) {
            var errorCode = 0
            val id = if (state.value!!.mode == ADD_MODE) {
                UUID.randomUUID().toString()
            } else {
                editMedicationModelId!!//todo?
            }
            val trackingType = getTrackingType()
            val medicationReminderTime = extractIntFromString(medicationReminderTime)
            val medicationFrequency = getFrequency()
            if (!trackingDataIsCorrect(trackingType)) errorCode = 5
            if (!medicationFrequency.isCorrect()) errorCode = 4
            if (intakeTimeList.isEmpty()) errorCode = 3
            if (dosage.isEmpty()) errorCode = 2
            if (medicationName.isEmpty()) errorCode = 1
            if (errorCode == 0) {
                return Pair(
                    MedicationModel(
                        id = id,
                        name = medicationName,
                        dosage = dosage.toDouble(),
                        dosageUnit = dosageUnits,
                        intakeTimes = intakeTimeList,
                        reminderTime = medicationReminderTime,
                        frequency = medicationFrequency,
                        selectedDays = selectedDays,
                        startDate = startIntakeDate.toDate(),
                        intakeType = getIntakeType(),
                        comment = medComment,
                        useBanner = useBannerChBox,
                        trackType = trackingType,
                        stockOfMedicine = getTrackingData().first,
                        numberOfDays = getTrackingData().second,
                        endDate = getTrackingData().third,
                    ), errorCode
                )
            } else {
                return Pair(null, errorCode)
            }
        }
    }

    private fun trackingDataIsCorrect(trackingType: MedicationModel.TrackType): Boolean {
        return when (trackingType) {
            MedicationModel.TrackType.STOCK_OF_MEDICINE -> state.value!!.stockOfMedicine
                .isNotEmpty()

            MedicationModel.TrackType.DATE -> state.value!!.endDate
                .isNotEmpty()

            MedicationModel.TrackType.NUMBER_OF_DAYS -> state.value!!.numberOfDays
                .isNotEmpty()

            MedicationModel.TrackType.NONE -> true
        }

    }

    /**На момент вызова getTrackingData() гарантируется, что соответствующее поле не пустое.
     *  Метод получает нужные данные, в зависимости от типа отслеживания*/
    private fun getTrackingData()
            : Triple<Double?, Double?, Date?> {
        with(_state.value!!) {
            val numberMedsStr = stockOfMedicine
            val numberDaysStr = numberOfDays
            val endIntakeDateStr = endDate
            val trackArray = resources.getStringArray(R.array.track_array)
            return when (trackType) {
                trackArray[0] -> Triple(null, null, null)
                trackArray[1] -> Triple(numberMedsStr.toDouble(), null, null)
                trackArray[2] -> Triple(null, numberDaysStr.toDouble(), null)
                trackArray[3] -> Triple(null, null, endIntakeDateStr.toDate())
                else -> Triple(null, null, null)
            }
        }
    }

    private fun getTrackingType()
            : MedicationModel.TrackType {
        val trackingTypeArray = resources.getStringArray(R.array.track_array)
        return when (state.value!!.trackType) {
            trackingTypeArray[0] -> MedicationModel.TrackType.NONE
            trackingTypeArray[1] -> MedicationModel.TrackType.STOCK_OF_MEDICINE
            trackingTypeArray[2] -> MedicationModel.TrackType.NUMBER_OF_DAYS
            trackingTypeArray[3] -> MedicationModel.TrackType.DATE
            else -> MedicationModel.TrackType.NONE
        }
    }

    private fun getFrequency()
            : MedicationModel.Frequency {
        val frequencyArray = resources.getStringArray(R.array.frequency_array)
        return when (state.value!!.frequency) {
            frequencyArray[0] -> MedicationModel.Frequency.SELECTED_DAYS
            frequencyArray[1] -> MedicationModel.Frequency.EVERY_OTHER_DAY
            frequencyArray[2] -> MedicationModel.Frequency.DAILY
            else -> MedicationModel.Frequency.SELECTED_DAYS
        }
    }

    private fun extractIntFromString(input: String)
            : Int {
        val regex = Regex("\\d+")
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull() ?: 0
    }

    private fun getIntakeType()
            : MedicationModel.IntakeType {
        val intakeTypeArray = resources.getStringArray(R.array.intake_array)
        return when (state.value!!.intakeType) {//todo state or _state??
            intakeTypeArray[0] -> MedicationModel.IntakeType.NONE
            intakeTypeArray[1] -> MedicationModel.IntakeType.BEFORE_MEAL
            intakeTypeArray[2] -> MedicationModel.IntakeType.DURING_MEAL
            intakeTypeArray[3] -> MedicationModel.IntakeType.AFTER_MEAL
            else -> MedicationModel.IntakeType.NONE
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
                    intakeTimeList = model.intakeTimes,
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

    private fun getCurrentDate()
            : String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return LocalDate.now().format(formatter)
    }

    /**### Функция *Double.toDisplayString()* убирает незначащие нули и возвращает строку готовую для отображения*/
    private fun Double.toDisplayString()
            : String {
        return if (this == this.toInt().toDouble()) {
            this.toInt().toString()
        } else {
            this.toString()
        }
    }

    /**### Функция *Date.toDisplayString()* приводит java.util.Date к строке вида dd.MM.yyyy*/
    private fun Date.toDisplayString()
            : String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(this)
    }

    /**### Функция *MedicationModel.IntakeType.toAdapterString()* переводит данные в строку из адаптера AutoCompleteTextView*/
    private fun MedicationModel.IntakeType.toAdapterString()
            : String {
        val intakeTypeArray = resources.getStringArray(R.array.intake_array)
        return when (this) {
            MedicationModel.IntakeType.NONE -> intakeTypeArray[0]
            MedicationModel.IntakeType.BEFORE_MEAL -> intakeTypeArray[1]
            MedicationModel.IntakeType.DURING_MEAL -> intakeTypeArray[2]
            MedicationModel.IntakeType.AFTER_MEAL -> intakeTypeArray[3]
        }
    }

    /**### Функция *Int.toAdapterString* переводит данные **REMINDER TYPE** в строку из адаптера AutoCompleteTextView*/
    private fun Int.toAdapterString()
            : String {
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
    private fun MedicationModel.TrackType.toAdapterString()
            : String {
        val trackTypeArray = resources.getStringArray(R.array.track_array)
        return when (this) {
            MedicationModel.TrackType.NONE -> trackTypeArray[0]
            MedicationModel.TrackType.STOCK_OF_MEDICINE -> trackTypeArray[1]
            MedicationModel.TrackType.NUMBER_OF_DAYS -> trackTypeArray[2]
            MedicationModel.TrackType.DATE -> trackTypeArray[3]
        }
    }

    /**### Функция *MedicationModel.Frequency.toAdapterString()* переводит данные в строку из адаптера AutoCompleteTextView*/
    private fun MedicationModel.Frequency.toAdapterString()
            : String {
        val frequencyTypeArray = resources.getStringArray(R.array.frequency_array)
        return when (this) {
            MedicationModel.Frequency.SELECTED_DAYS -> frequencyTypeArray[0]
            MedicationModel.Frequency.EVERY_OTHER_DAY -> frequencyTypeArray[1]
            MedicationModel.Frequency.DAILY -> frequencyTypeArray[2]
        }
    }

    private fun String.toDate()
            : Date {
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val localDate = LocalDate.parse(this, format)
        val instant = localDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
        return Date.from(instant)
    }

    private fun MedicationModel.Frequency.isCorrect()
            : Boolean {
        return when (this) {
            MedicationModel.Frequency.DAILY -> true
            MedicationModel.Frequency.EVERY_OTHER_DAY -> true
            MedicationModel.Frequency.SELECTED_DAYS -> state.value!!.selectedDays.isNotEmpty()
        }
    }
}