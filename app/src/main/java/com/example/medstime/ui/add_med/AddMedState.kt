package com.example.medstime.ui.add_med

import com.example.domain.models.MedicationModel

data class AddMedState(
    val isSavedNewMedication: Boolean,
    val mode: String,
    val inputError: Int,
    val medicationName: String,
    val dosage: String,
    val dosageUnits: String,
    val startIntakeDate: String,
    val medComment: String,
    val useBannerChBox: Boolean,
    val intakeTimeList: List<MedicationModel.Time>,
    val medicationReminderTime: String,
    val intakeType: String,
    val frequency: String,
    val selectedDays: List<Int>,
    val trackType: String,
    val stockOfMedicine: String,
    val numberOfDays: String,
    val endDate: String,
    val trackModelId: String?,
) {
    companion object {
        const val EDIT_MODE = "edit"
        const val ADD_MODE = "add"
    }
}



