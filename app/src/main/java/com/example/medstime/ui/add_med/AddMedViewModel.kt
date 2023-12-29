package com.example.medstime.ui.add_med

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.domain.usecase.common.GetMedicationById
import com.example.domain.usecase.common.ReplaceMedicationModel
import com.example.domain.usecase.common.SaveNewMedication
import com.example.medstime.R
import com.example.medstime.ui.add_med.AddMedState.Companion.ADD_MODE
import com.example.medstime.ui.add_med.AddMedState.Companion.EDIT_MODE
import com.example.medstime.ui.utils.generateStringId
import com.example.medstime.ui.utils.getCurrentDateString
import com.example.medstime.ui.utils.toDate
import com.example.medstime.ui.utils.toDisplayString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    private var editMedicationModelId: String? = null

    init {
        _state.value = AddMedState(startIntakeDate = getCurrentDateString())
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
            is AddMedEvent.AddMedTrackButtonClicked -> {

            }

            is AddMedEvent.ContinueButtonClicked -> {
                saveMedicationModel()
            }

            is AddMedEvent.ErrorWasShown -> {
                _state.value = _state.value!!.copy(inputError = 0)
            }

            is AddMedEvent.Mode -> {
                if (event.mode == EDIT_MODE) {
                    editMedicationModelId = event.medicationModelId
                    getMedicationModelById(event.medicationModelId)
                }
            }

            is AddMedEvent.UpdateState -> {
                _state.value = event.state
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


    //todo протестировать и перепроверить
    //todo получение данных происходит из фрагмента? Добавляется поле medsTrackModel через другой фрагмент?
    private fun makeMedicationModel(): Pair<MedicationModel?, Int> {
        with(state.value!!) {
            var errorCode = 0
            val id = if (state.value!!.mode == ADD_MODE) {
                generateStringId()
            } else {
                editMedicationModelId!!//todo?
            }
            val trackingType = getTrackingType()
            val medicationReminderTime = extractIntFromString(medicationReminderTime)
            val medicationFrequency = getFrequency()
            val trackModel = MedsTrackModel()
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
                        startDate = startIntakeDate.toDate().time,
                        intakeType = getIntakeType(),
                        comment = medComment,
                        useBanner = useBannerChBox,
                        trackModel = trackModel,
                    ), errorCode
                )
            } else {
                return Pair(null, errorCode)
            }
        }
    }

    private fun trackingDataIsCorrect(trackingType: MedsTrackModel.TrackType): Boolean {
        return when (trackingType) {
            MedsTrackModel.TrackType.STOCK_OF_MEDICINE -> state.value!!.stockOfMedicine != -1.0

            MedsTrackModel.TrackType.DATE -> state.value!!.endDate.isNotEmpty()

            MedsTrackModel.TrackType.NUMBER_OF_DAYS -> state.value!!.numberOfDays != -1

            MedsTrackModel.TrackType.PACKAGES_TRACK -> state.value!!.packageItems
                .isNotEmpty()

            MedsTrackModel.TrackType.NONE -> true
        }
    }

//    /**На момент вызова getTrackingData() гарантируется, что соответствующее поле не пустое.
//     *  Метод получает нужные данные, в зависимости от типа отслеживания*/
//    private fun getTrackingData(): Triple<Double?, Double?, Date?> {
//        with(_state.value!!) {
//            val numberMedsStr = stockOfMedicine
//            val numberDaysStr = numberOfDays
//            val endIntakeDateStr = endDate
//            val trackArray = resources.getStringArray(R.array.track_array)
//            return when (trackType) {
//                trackArray[0] -> Triple(null, null, null)
//                trackArray[1] -> Triple(numberMedsStr.toDouble(), null, null)
//                trackArray[2] -> Triple(null, numberDaysStr.toDouble(), null)
//                trackArray[3] -> Triple(null, null, endIntakeDateStr.toDate())
//                else -> Triple(null, null, null)
//            }
//        }
//    }

    private fun getTrackingType()
            : MedsTrackModel.TrackType {
        val trackingTypeArray = resources.getStringArray(R.array.track_array)
        return when (state.value!!.trackType) {
            trackingTypeArray[0] -> MedsTrackModel.TrackType.NONE
            trackingTypeArray[1] -> MedsTrackModel.TrackType.STOCK_OF_MEDICINE
            trackingTypeArray[2] -> MedsTrackModel.TrackType.NUMBER_OF_DAYS
            trackingTypeArray[3] -> MedsTrackModel.TrackType.DATE
            else -> MedsTrackModel.TrackType.NONE
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

    /*****getMedicationModelById()*** получает **MedicationModel**, если фрагмент открыт в режиме редактирования.
     * Также он парсит модель и изменяет ***_state***. */
    private fun getMedicationModelById(medicationModelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = getMedicationModelUseCase.invoke(medicationModelId)
            val selectedDays = model.selectedDays ?: listOf()
            var newState = state.value!!
            newState = newState.copy(
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
                trackType = model.trackModel.trackType.toAdapterString(),
                mode = EDIT_MODE,
                packageItems = model.trackModel.packageItems,
                packageCounter = model.trackModel.packageCounter,
                stockOfMedicine = model.trackModel.stockOfMedicine,
                numberOfDays = model.trackModel.numberOfDays,
                endDate = model.trackModel.endDate.toDisplayString(),
            )

//            when (model.trackType) {
//                MedicationModel.TrackType.STOCK_OF_MEDICINE -> {
//                    //todo
//                }
//
//                MedicationModel.TrackType.DATE -> {
//                    newState =
//                        newState.copy(endDate = model.trackModel.endDate.toDisplayString())
//                }
//
//                MedicationModel.TrackType.NUMBER_OF_DAYS -> {
//                    newState =
//                        newState.copy(numberOfDays = model.trackModel.numberOfDays.toString())
//                }
//
//                MedicationModel.TrackType.NONE -> {
//
//                }
//            }
//            val stockOfMedicine = model.trackModel.stockOfMedicine.toDisplayString()

            _state.postValue(newState)
        }
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
    private fun MedsTrackModel.TrackType.toAdapterString(): String {
        val trackTypeArray = resources.getStringArray(R.array.track_array)
        return when (this) {
            MedsTrackModel.TrackType.NONE -> trackTypeArray[0]
            MedsTrackModel.TrackType.STOCK_OF_MEDICINE -> trackTypeArray[1]
            MedsTrackModel.TrackType.NUMBER_OF_DAYS -> trackTypeArray[2]
            MedsTrackModel.TrackType.DATE -> trackTypeArray[3]
            MedsTrackModel.TrackType.PACKAGES_TRACK -> trackTypeArray[4]
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

    private fun MedicationModel.Frequency.isCorrect(): Boolean {
        return when (this) {
            MedicationModel.Frequency.DAILY -> true
            MedicationModel.Frequency.EVERY_OTHER_DAY -> true
            MedicationModel.Frequency.SELECTED_DAYS -> state.value!!.selectedDays.isNotEmpty()
        }
    }
}
