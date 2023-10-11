package com.example.medstime.ui.add_med

import com.example.domain.models.MedicationModel

sealed class AddMedEvent {
    data class MedicationModelSaved(val isSaved: Boolean) : AddMedEvent()
    data class Mode(val mode: String, val medicationModelId: String) : AddMedEvent()
    data class MedicationNameChanged(val medicationName: String) : AddMedEvent()
    data class DosageChanged(val newDosage: String) : AddMedEvent()
    data class DosageUnitsChanged(val newDosageUnits: String) : AddMedEvent()
    data class StartIntakeDateChanged(val newStartIntakeDate: String) : AddMedEvent()
    data class MedCommentChanged(val newMedComment: String) : AddMedEvent()
    data class MedicationReminderTimeChanged(val newReminderTime: String) : AddMedEvent()
    data class UseBannerCheckBoxChanged(val newUseBanner: Boolean) : AddMedEvent()
    data class FrequencyChanged(val newFrequency: String) : AddMedEvent()
    data class SelectedDaysChanged(val newSelectedDays: List<Int>) : AddMedEvent()
    data class IntakeTimeChanged(val newIntakeTime: List<MedicationModel.Time>) : AddMedEvent()
    data class TrackTypeChanged(val newTrackType: String) : AddMedEvent()
    data class StockOfMedicineChanged(val newStock: String) : AddMedEvent()
    data class NumberOfDaysChanged(val newNumberOfDays: String) : AddMedEvent()
    data class EndDateChanged(val newEndDate: String) : AddMedEvent()
}
