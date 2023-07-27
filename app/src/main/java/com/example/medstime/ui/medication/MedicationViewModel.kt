package com.example.medstime.ui.medication

import androidx.lifecycle.ViewModel
import com.example.medstime.domain.models.MedicationModel
import com.example.medstime.domain.usecase.medication.GetMedicationsList
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem
import java.util.Date

class MedicationViewModel(
    getMedicationListUseCase: GetMedicationsList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem
) : ViewModel() {
    val medicationsList: List<MedicationModel> = getMedicationListUseCase.invoke()
    private fun removeMedication(medicationModel: MedicationModel) {

    }

    private fun getTodayMedications(date: Date) {
        
    }

}