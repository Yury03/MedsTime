package com.example.medstime.ui.add_track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.PackageItemModel
import com.example.domain.usecase.meds_track.GetTrackById
import com.example.medstime.ui.utils.generateStringId
import com.example.medstime.ui.utils.toDate
import kotlinx.coroutines.launch

class AddMedTrackViewModel(
    private val getMedTrackByIdUseCase: GetTrackById
) : ViewModel() {
    private val _state: MutableLiveData<AddMedTrackState> =
        MutableLiveData()
    val state: LiveData<AddMedTrackState>
        get() = _state

    init {
        _state.value = AddMedTrackState(
            medName = "",
            medTrack = null,
            medsTrackId = null,
            actualPackageList = emptyList(),
            expirationDate = "",
            dosageUnit = "",
            quantityInPackage = "",
            errorCode = AddMedTrackState.VALID,
        )
    }

    private fun updateErrorCode() =
        when {
//        medName.isEmpty() -> {
//            false
//        }
            state.value!!.expirationDate.isEmpty() -> {
                AddMedTrackState.EXPIRATION_DATE
            }

            state.value!!.quantityInPackage.isEmpty() -> {
                AddMedTrackState.QUANTITY_IN_PACKAGE
            }

            else -> AddMedTrackState.VALID
        }


    fun send(event: AddMedTrackEvent) {
        when (event) {
            AddMedTrackEvent.AddNewPackageButtonClicked -> {
                val errorCode = updateErrorCode()
                val currentPackageList = mutableListOf<PackageItemModel>().apply {
                    addAll(state.value!!.actualPackageList)
                }
                if (errorCode == AddMedTrackState.VALID) {
                    currentPackageList.add(
                        PackageItemModel(
                            id = generateStringId(),
                            expirationDate = state.value!!.expirationDate.toDate().time
                        )
                    )
                    _state.postValue(
                        state.value!!.copy(
                            errorCode = errorCode,
                            actualPackageList = currentPackageList,
                        )
                    )
                } else {
                    _state.postValue(state.value!!.copy(errorCode = errorCode))
                }

            }

            is AddMedTrackEvent.UpdateState -> {
                _state.value = event.state
            }

            is AddMedTrackEvent.HandleArguments -> {
                event.medsTrackModelId?.let { medsTrackModelId ->
                    getMedsTrackModelById(medsTrackModelId)
                }
                _state.postValue(
                    state.value!!.copy(
                        medName = event.medName,
                        medsTrackId = event.medsTrackModelId,
                        dosageUnit = event.dosageUnits,
                    )
                )
            }
        }
    }

    private fun getMedsTrackModelById(medsTrackModelId: String) {
        viewModelScope.launch {
            val medTrack = getMedTrackByIdUseCase.invoke(medsTrackModelId)
            _state.postValue(
                state.value!!.copy(
                    medTrack = medTrack,
                    actualPackageList = medTrack.packageItems,//todo test
                    medName = medTrack.name,//todo test
                )
            )
        }
    }
}