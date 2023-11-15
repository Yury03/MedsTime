package com.example.medstime.ui.add_track

sealed class AddMedTrackEvent {
    data object AddNewPackageButtonClicked : AddMedTrackEvent()
    data class UpdateState(val state: AddMedTrackState) : AddMedTrackEvent()
    data class HandleArguments(
        val medName: String,
        val dosageUnits: String,
        val medsTrackModelId: String?,
    ) : AddMedTrackEvent()

}