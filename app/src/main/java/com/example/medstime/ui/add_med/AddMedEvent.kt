package com.example.medstime.ui.add_med

import com.example.domain.models.MedicationModel

sealed class AddMedEvent {
    data object AddMedTrackButtonClicked : AddMedEvent()
    data object ContinueButtonClicked : AddMedEvent()
    data object ErrorWasShown : AddMedEvent()
    data class Mode(val mode: String, val medicationModelId: String) : AddMedEvent()
    data class RestoreState(val state: AddMedState) : AddMedEvent()
    data class MedicationModelChanged(
        val medicationName: String,
        val newDosage: String,
        val newDosageUnits: String,
        val newStartIntakeDate: String,
        val newMedComment: String,
        val newReminderTime: String,
        val newUseBanner: Boolean,
        val newFrequency: String,
        val newSelectedDays: List<Int>,
        val newIntakeTime: List<MedicationModel.Time>,
        val newTrackType: String,
        val newNumberOfDays: String,
        val newEndDate: String,
    ) : AddMedEvent()
}
