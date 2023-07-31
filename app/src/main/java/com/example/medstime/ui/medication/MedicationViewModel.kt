package com.example.medstime.ui.medication

import androidx.lifecycle.ViewModel
import com.example.medstime.domain.models.MedicationModel
import com.example.medstime.domain.usecase.medication.GetIntakeList
import com.example.medstime.domain.usecase.medication.GetMedicationById
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationIntake
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem
import java.util.Date

class MedicationViewModel(
    getIntakeList: GetIntakeList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem,
    getMedicationById: GetMedicationById,
    replaceMedicationIntake: ReplaceMedicationIntake,
) : ViewModel() {
    val medicationsList: List<MedicationModel> = getMedicationListUseCase.invoke()
    private fun removeMedication(medicationModel: MedicationModel) {

    }

    private fun getTodayMedications(date: Date) {
        
    }

}