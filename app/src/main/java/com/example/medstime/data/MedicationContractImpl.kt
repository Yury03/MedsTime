package com.example.medstime.data

import android.content.Context
import androidx.room.Room
import com.example.medstime.data.mappers.MedicationIntakeMapper
import com.example.medstime.data.mappers.MedicationMapper
import com.example.medstime.data.room.MedicationIntakeDatabase
import com.example.medstime.data.room.MedicationsDatabase
import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.domain.models.MedicationModel
import kotlinx.coroutines.Dispatchers

class MedicationContractImpl(context: Context) : Repository.MedicationContract {
    private val medicationDatabase = Room.databaseBuilder(
        context,
        MedicationsDatabase::class.java, "Medication"
    ).build()//todo убрать из Main Thread

    private val medicationIntakeDatabase = Room.databaseBuilder(
        context,
        MedicationIntakeDatabase::class.java, "MedicationIntake"
    ).build()//todo убрать из Main Thread

    private val medicationDao = medicationDatabase.medicationDao()
    private val medicationIntakeDao = medicationIntakeDatabase.medicationIntake()

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