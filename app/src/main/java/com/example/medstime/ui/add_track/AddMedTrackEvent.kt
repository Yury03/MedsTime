package com.example.medstime.ui.add_track

sealed class AddMedTrackEvent {
    data object AddNewPackageButtonClicked : AddMedTrackEvent()
    data object BackButtonClicked : AddMedTrackEvent()
    data class UpdateState(val state: AddMedTrackState) : AddMedTrackEvent()
    data class HandleArguments(
        val addMedStateJsonString: String,
    ) : AddMedTrackEvent()
}