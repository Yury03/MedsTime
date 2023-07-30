package com.example.medstime.domain

import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.domain.models.MedicationModel

interface Repository {
    interface MedicationContract {
        fun getIntakeList(): List<MedicationIntakeModel>
        fun replaceMedicationItem(medicationModel: MedicationModel)
        fun removeMedicationItem(medicationModel: MedicationModel)
        fun getMedicationById(id: String): MedicationModel
        fun replaceMedicationIntake(medicationIntakeModel: MedicationIntakeModel)
    }
}