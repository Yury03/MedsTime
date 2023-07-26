package com.example.medstime.ui.medication

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.medstime.data.MedicationContractImpl
import com.example.medstime.domain.models.MedicationModel
import com.example.medstime.domain.usecase.medication.GetMedicationsList
import com.example.medstime.domain.usecase.medication.RemoveMedicationItem
import com.example.medstime.domain.usecase.medication.ReplaceMedicationItem

class MedicationViewModel(
    getMedicationListUseCase: GetMedicationsList,
    removeMedicationItemUseCase: RemoveMedicationItem,
    replaceMedicationItemUseCase: ReplaceMedicationItem
) : ViewModel() {
    private fun removeMedication(medicationModel: MedicationModel) {

    }
}