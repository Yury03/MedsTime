package com.example.medstime.ui.add_track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.PackageItemModel
import com.example.domain.usecase.meds_track.GetTrackById
import com.example.medstime.ui.utils.generateStringId
import com.example.medstime.ui.utils.toDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMedTrackViewModel(
    private val getMedTrackByIdUseCase: GetTrackById
) : ViewModel() {
    private val _state: MutableStateFlow<AddMedTrackState> =
        MutableStateFlow(AddMedTrackState())
    val state: StateFlow<AddMedTrackState> = _state.asStateFlow()



    private fun updateErrorCode() =
        when {
//        medName.isEmpty() -> {
//            false
//        }
            _state.value.expirationDate.isEmpty() -> {
                AddMedTrackState.EXPIRATION_DATE
            }

            _state.value.quantityInPackage.isEmpty() -> {
                AddMedTrackState.QUANTITY_IN_PACKAGE
            }

            else -> AddMedTrackState.VALID
        }


    fun send(event: AddMedTrackEvent) {
        when (event) {
            AddMedTrackEvent.AddNewPackageButtonClicked -> {
                val errorCode = updateErrorCode()
                val currentPackageList = mutableListOf<PackageItemModel>().apply {
                    addAll(_state.value.actualPackageList)
                }
                if (errorCode == AddMedTrackState.VALID) {
                    currentPackageList.add(
                        PackageItemModel(
                            id = generateStringId(),
                            expirationDate = _state.value.expirationDate.toDate().time
                        )
                    )
                    _state.update { currentState ->
                        currentState.copy(
                            errorCode = errorCode,
                            actualPackageList = currentPackageList,
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
                event.medsTrackModelId?.let { medsTrackModelId ->
                    getMedsTrackModelById(medsTrackModelId)
                }
                _state.update { currentState ->
                    currentState.copy(
                        medName = event.medName,
                        medsTrackId = event.medsTrackModelId,
                        dosageUnit = event.dosageUnits,
                    )
                }
            }
        }
    }

    private fun getMedsTrackModelById(medsTrackModelId: String) {
        viewModelScope.launch {
            val medTrack = getMedTrackByIdUseCase.invoke(medsTrackModelId)
            _state.update { currentState ->
                currentState.copy(
                    medTrack = medTrack,
                    actualPackageList = medTrack.packageItems,//todo test
                    medName = medTrack.name,//todo test
                )
            }
        }
    }
}