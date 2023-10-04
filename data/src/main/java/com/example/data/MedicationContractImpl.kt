package com.example.data

import android.content.Context
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.mappers.MedicationMapper
import com.example.data.room.MedicationDatabase
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.dao.MedicationDao
import com.example.data.room.dao.MedicationIntakeDao
import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel

class MedicationContractImpl(context: Context) : Repository.MedicationContract {
    private val medicationDatabase: MedicationDatabase by lazy {
        MedicationDatabase.getDatabase(context)
    }
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }

    private val medicationDao: MedicationDao by lazy { medicationDatabase.medicationDao() }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }

    override fun getIntakeList(): List<MedicationIntakeModel> {
        val medicationIntakeList = medicationIntakeDao.getAll()
        return medicationIntakeList.map { MedicationIntakeMapper.mapToModel(it) }
    }

    override fun replaceMedicationItem(medicationModel: MedicationModel) {
        val addingEntity = MedicationMapper.mapToEntity(medicationModel)
        medicationDao.deleteById(medicationModel.id)
        medicationDao.insert(addingEntity)
    }

    override fun removeMedicationItem(medicationModel: MedicationModel) {
        val deletingEntity = MedicationMapper.mapToEntity(medicationModel)
        medicationIntakeDao.deleteByMedicationModelId(medicationModel.id)   //при удалении модели приема удаляются
        medicationDao.delete(deletingEntity)                                //все связанные intake модели
    }

    override fun getMedicationById(id: String): MedicationModel {
        return MedicationMapper.mapToModel(medicationDao.getById(id))
    }

    override fun replaceMedicationIntake(medicationIntakeModel: MedicationIntakeModel) {
        val medicationIntake = MedicationIntakeMapper.mapToEntity(medicationIntakeModel)
        medicationIntakeDao.deleteById(medicationIntakeModel.id)
        medicationIntakeDao.insert(medicationIntake)
    }

}