package com.example.data

import android.content.Context
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.mappers.MedicationMapper
import com.example.data.room.MedicationDatabase
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.MedicationDao
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.dao.ReminderDao
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
    private val reminderDatabase: ReminderDatabase by lazy {
        ReminderDatabase.getDatabase(context)
    }

    private val medicationDao: MedicationDao by lazy { medicationDatabase.medicationDao() }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }
    private val reminderDao: ReminderDao by lazy { reminderDatabase.reminderDao() }

    override fun getIntakeList(): List<MedicationIntakeModel> {
        val medicationIntakeList = medicationIntakeDao.getAll()
        return medicationIntakeList.map { MedicationIntakeMapper.mapToModel(it) }
    }

    /**Метод **removeMedicationModel**
     * - получает все *приемы лекарств*
     * - удаляет все напоминания по *id приема лекарств*
     * - удаляет все *приемы лекарств по id модели Medication*
     * - удаляет модель Medication*/
    override fun removeMedicationModel(medicationModelId: String) {
        medicationIntakeDao.getByMedicationModelId(medicationModelId).forEach {
            reminderDao.deleteByMedicationIntakeModelId(it.id)
        }
        medicationIntakeDao.deleteByMedicationModelId(medicationModelId)
        medicationDao.deleteById(medicationModelId)
    }

    override fun getMedicationById(id: String): MedicationModel {
        return MedicationMapper.mapToModel(medicationDao.getById(id))
    }

    override fun changeActualTimeIntake(
        medicationIntakeId: String,
        newTime: MedicationIntakeModel.Time
    ) {
        medicationIntakeDao.updateActualIntakeTimeById(medicationIntakeId, newTime.toEntityString())
    }

}