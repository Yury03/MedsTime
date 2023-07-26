package com.example.medstime.domain

import com.example.medstime.domain.models.MedicationModel

interface Repository {
    interface MedicationContract {
        fun getMedicationsList(): List<MedicationModel>
        fun replaceMedicationItem(medicationModel: MedicationModel)
        fun removeMedicationItem(medicationModel: MedicationModel)
    }
}