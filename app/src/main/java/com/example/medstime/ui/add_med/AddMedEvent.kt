package com.example.medstime.ui.add_med

sealed class AddMedEvent {
    data object ContinueButtonClicked : AddMedEvent()
    data object ErrorWasShown : AddMedEvent()
    data class Mode(val mode: String, val medicationModelId: String) : AddMedEvent()
    data class UpdateState(val state: AddMedState) : AddMedEvent()
}
