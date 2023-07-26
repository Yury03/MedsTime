package com.example.medstime.data

import android.content.Context
import androidx.room.Room
import com.example.medstime.data.mappers.MedicationMapper
import com.example.medstime.data.room.MedicationsDatabase
import com.example.medstime.data.room.entity.MedicationEntity
import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationModel

class MedicationContractImpl(context: Context) : Repository.MedicationContract {
    private val db = Room.databaseBuilder(
        context,
        MedicationsDatabase::class.java, "Medications"
    ).build()
    private val medicationDao = db.medicationDao()
    override fun getMedicationsList(): List<MedicationModel> {
        val medicationsList: List<MedicationEntity> = medicationDao.getAll()
        return medicationsList.map { MedicationMapper.mapToModel(it) }
    }

    override fun replaceMedicationItem(medicationModel: MedicationModel) {
        medicationDao.deleteById(medicationModel.id)
        val addingEntity = MedicationMapper.mapToEntity(medicationModel)
        medicationDao.insert(addingEntity)
    }

    override fun removeMedicationItem(medicationModel: MedicationModel) {
        val deletingEntity = MedicationMapper.mapToEntity(medicationModel)
        medicationDao.delete(deletingEntity)
    }
}