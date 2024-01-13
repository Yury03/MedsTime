package com.example.medstime.ui.add_med

import com.example.domain.models.MedicationModel
import com.example.domain.models.PackageItemModel

data class AddMedState(
    val isSavedNewMedication: Boolean = false,
    val mode: String = ADD_MODE,
    val inputError: Int = 0,
    val medicationName: String = "",
    val dosage: String = "",
    val dosageUnits: String = "",
    val startIntakeDate: String = "",
    val medComment: String = "",
    val useBannerChBox: Boolean = false,
    val intakeTimeList: List<MedicationModel.Time> = listOf(),
    val medicationReminderTime: String = "",
    val intakeType: String = "",
    val frequency: String = "",
    val selectedDays: List<Int> = listOf(),
    val packageItems: MutableList<PackageItemModel> = mutableListOf(),
    val packageCounter: Int = 0,
    val stockOfMedicine: Double = 0.0,
    val numberOfDays: Int = 0,
    val endDate: String = "",
    val trackType: String = "",
) {
    companion object {
        const val EDIT_MODE = "edit"
        const val ADD_MODE = "add"
    }
}



