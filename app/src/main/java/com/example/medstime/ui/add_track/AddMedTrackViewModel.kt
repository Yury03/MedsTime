package com.example.medstime.ui.add_track

import androidx.lifecycle.ViewModel
import com.example.domain.models.PackageItemModel
import com.example.domain.usecase.meds_track.GetTrackById
import com.example.medstime.ui.add_med.AddMedState
import com.example.medstime.ui.utils.generateStringId
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class AddMedTrackViewModel(private val getMedTrackByIdUseCase: GetTrackById) : ViewModel() {
    private val _state: MutableStateFlow<AddMedTrackState> =
        MutableStateFlow(AddMedTrackState())
    val state: StateFlow<AddMedTrackState> = _state.asStateFlow()
    private lateinit var addMedState: AddMedState

    private fun updateErrorCode(): Int {
        val stateValue = _state.value
        val currentTimeLong = Date().time
        return when {
            stateValue.expirationDate == 0L -> {
                AddMedTrackState.EXPIRATION_DATE_IS_EMPTY
            }

            stateValue.expirationDate <= currentTimeLong -> {
                AddMedTrackState.EXPIRATION_DATE_TOO_SMALL
            }

            stateValue.quantityInPackage.isEmpty() -> {
                AddMedTrackState.QUANTITY_IN_PACKAGE_IS_EMPTY
            }

            else -> AddMedTrackState.VALID
        }
    }

    fun send(event: AddMedTrackEvent) {
        when (event) {
            AddMedTrackEvent.AddNewPackageButtonClicked -> {
                val errorCode = updateErrorCode()
                val currentPackageList = mutableListOf<PackageItemModel>().apply {
                    addAll(_state.value.actualPackageList)
                }
                if (errorCode == AddMedTrackState.VALID) {
                    //добавление упаковки лекарств
                    currentPackageList.add(
                        PackageItemModel(
                            id = generateStringId(),
                            expirationDate = _state.value.expirationDate
                        )
                    )
                    //обновление состояния
                    _state.update { currentState ->
                        currentState.copy(
                            errorCode = errorCode,
                            actualPackageList = currentPackageList,
                            expirationDate = 0,
                            quantityInPackage = "",
                        )
                    }
                } else {
                    _state.update { currentState ->
                        currentState.copy(errorCode = errorCode)
                    }
                }
            }

            is AddMedTrackEvent.UpdateState -> {
                _state.update {
                    event.state
                }
            }

            is AddMedTrackEvent.HandleArguments -> {
                addMedState =
                    Gson().fromJson(event.addMedStateJsonString, AddMedState::class.java)
                _state.update { currentState ->//todo почистить
                    currentState.copy(
                        medName = addMedState.medicationName,
                        dosageUnit = addMedState.dosageUnits,
//                        medTrack = addMedState.trackModel,
                    )
                }
            }
            /***/
            AddMedTrackEvent.BackButtonClicked -> {
                _state.update { currentState ->
                    //изменение состояния AddMedFragment
                    addMedState = addMedState.copy(
                        medicationName = currentState.medName,
                        dosageUnits = currentState.dosageUnit,
//                        trackModel = currentState.medTrack,
                    )
                    currentState.copy(addMedStateJson = getActualAddMedStateJson())
                }
            }
        }
    }

    private fun getActualAddMedStateJson(): String {
        return Gson().toJson(addMedState)
    }

//    private fun pullMedsTrackModelById(medsTrackModelId: String) {
//        viewModelScope.launch {
//            val medTrack = getMedTrackByIdUseCase.invoke(medsTrackModelId)
//            _state.update { currentState ->
//                currentState.copy(
//                    medTrack = medTrack,
//                    actualPackageList = medTrack.packageItems,
//                    medName = medTrack.name,
//                )
//            }
//        }
//    }
}