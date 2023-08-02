package com.example.medstime.data

import android.content.Context
import com.example.medstime.data.mappers.MedicationIntakeMapper
import com.example.medstime.data.mappers.MedicationMapper
import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.domain.models.MedicationModel
import java.util.Date

class MedicationContractImpl(context: Context) : Repository.MedicationContract {
//    private val medicationDatabase = Room.databaseBuilder(
//        context,
//        MedicationsDatabase::class.java, "Medication"
//    ).build()//todo убрать из Main Thread
//
//    private val medicationIntakeDatabase = Room.databaseBuilder(
//        context,
//        MedicationIntakeDatabase::class.java, "MedicationIntake"
//    ).build()//todo убрать из Main Thread

//    private val medicationDao = medicationDatabase.medicationDao()
//    private val medicationIntakeDao = medicationIntakeDatabase.medicationIntake()

    override fun getIntakeList(): List<MedicationIntakeModel> {
//        val medicationIntakeList = medicationIntakeDao.getAll()
//        return medicationIntakeList.map { MedicationIntakeMapper.mapToModel(it) }
        return listOf(
            MedicationIntakeModel(
                id = "412",
                name = "Артроверон адванс",
                dosage = 2.5,
                dosageUnit = "таб.",
                isTaken = false,
                reminderTime = 4,
                medicationId = 3,
                intakeTime = MedicationIntakeModel.Time(15, 30),
                intakeDate = MedicationIntakeModel.Date(19, 7),
                actualIntakeDate = null,
                actualIntakeTime = null,
                intakeType = MedicationIntakeModel.IntakeType.AFTER_MEAL,
            ),
            MedicationIntakeModel(
                id = "322",
                name = "Триалгин",
                dosage = 2.0,
                dosageUnit = "кап.",
                isTaken = true,
                reminderTime = 4,
                medicationId = 3,
                intakeTime = MedicationIntakeModel.Time(15, 30),
                intakeDate = MedicationIntakeModel.Date(19, 7),
                actualIntakeDate = MedicationIntakeModel.Date(19, 7),
                actualIntakeTime = MedicationIntakeModel.Time(3, 20),
                intakeType = MedicationIntakeModel.IntakeType.BEFORE_MEAL,
            ),
            MedicationIntakeModel(
                id = "3234",
                name = "Ибуклин",
                dosage = 500.0,
                dosageUnit = "мг",
                isTaken = false,
                reminderTime = 10,
                medicationId = 3,
                intakeTime = MedicationIntakeModel.Time(18, 0),
                intakeDate = MedicationIntakeModel.Date(19, 7),
                actualIntakeDate = null,
                actualIntakeTime = null,
                intakeType = MedicationIntakeModel.IntakeType.AFTER_MEAL,
            ),
        )
    }

    override fun replaceMedicationItem(medicationModel: MedicationModel) {
        val addingEntity = MedicationMapper.mapToEntity(medicationModel)
//        medicationDao.deleteById(medicationModel.id)
//        medicationDao.insert(addingEntity)
    }

    override fun removeMedicationItem(medicationModel: MedicationModel) {
        val deletingEntity = MedicationMapper.mapToEntity(medicationModel)
//        medicationIntakeDao.deleteByMedicationModelId(medicationModel.id)   //при удалении модели приема удаляются
//        medicationDao.delete(deletingEntity)                                //все связанные intake модели
    }

    override fun getMedicationById(id: String): MedicationModel {
//        return MedicationMapper.mapToModel(medicationDao.getById(id))
        return MedicationModel(
            id = "322",
            name = "name",
            dosage = 32.3,
            dosageUnit = "t",
            reminderTime = 4,
            intakeType = MedicationModel.IntakeType.AFTER_MEAL,
            startDate = Date(),
            endDate = null,
            selectedDays = null,
            frequency = MedicationModel.Frequency.DAILY,
            intakeTimes = listOf(MedicationModel.Time(3, 32))
        )
    }

    override fun replaceMedicationIntake(medicationIntakeModel: MedicationIntakeModel) {
        val medicationIntake = MedicationIntakeMapper.mapToEntity(medicationIntakeModel)
//        medicationIntakeDao.deleteById(medicationIntakeModel.id)
//        medicationIntakeDao.insert(medicationIntake)
    }

}