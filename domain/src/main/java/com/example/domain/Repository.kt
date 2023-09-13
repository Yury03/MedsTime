package com.example.domain

import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel

interface Repository {
    interface MedicationContract {
        fun getIntakeList(): List<MedicationIntakeModel>
        fun replaceMedicationItem(medicationModel: MedicationModel)
        fun removeMedicationItem(medicationModel: MedicationModel)
        fun getMedicationById(id: String): MedicationModel
        fun replaceMedicationIntake(medicationIntakeModel: MedicationIntakeModel)
    }

    interface AdditionContract {
        fun saveNewMedication(medicationModel: MedicationModel)
    }


}