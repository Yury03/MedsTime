package com.example.data

import android.content.Context
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.mappers.MedicationMapper
import com.example.data.room.MedicationDatabase
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.dao.MedicationDao
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.entity.MedicationEntity
import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel
import java.util.Date
import java.util.concurrent.TimeUnit

class AdditionalContractImpl(context: Context) : Repository.AdditionContract {
    private val medicationDatabase: MedicationDatabase by lazy {
        MedicationDatabase.getDatabase(context)
    }
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }
    private val medicationDao: MedicationDao by lazy { medicationDatabase.medicationDao() }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntake() }
    override fun saveNewMedication(medicationModel: MedicationModel) {
        val entity = MedicationMapper.mapToEntity(medicationModel)
        medicationDao.insert(entity)
        generateMedicationEntity(entity, medicationModel)
    }

    private fun generateMedicationEntity(entity: MedicationEntity, model: MedicationModel) {
        //todo ошибки генерации даты: месяц на один меньше
        //todo дни - рандом от 0 до 7?
        val name = model.name
        val dosage = model.dosage
        val dosageUnit = model.dosageUnit
        val reminderTime = model.reminderTime
        val id = model.id
        val intakeType = model.intakeType
        val days = model.endDate?.let {
            TimeUnit.MILLISECONDS.toDays(it.time - model.startDate.time)
        } ?: 14//по дефолту генерируем только на 14 дней
        val listIntake = mutableListOf<MedicationIntakeModel>()
        for (i in 0 until days) {
            val date = Date(model.startDate.time + TimeUnit.DAYS.toMillis(i))
            model.intakeTimes.forEach {
                val intake = MedicationIntakeModel(
                    id = generateUniqueId(),
                    name = name,
                    dosage = dosage,
                    dosageUnit = dosageUnit,
                    isTaken = false,
                    reminderTime = reminderTime,
                    medicationId = id,
                    intakeTime = MedicationIntakeModel.Time(it.hour, it.minute),
                    intakeDate = MedicationIntakeModel.Date(date.day, date.month),//TODO
                    actualIntakeTime = null,
                    actualIntakeDate = null,
                    intakeType = MedicationIntakeModel.IntakeType.valueOf(intakeType.toString())//TODO
                )
                listIntake.add(intake)
            }
        }
        if (listIntake.isNotEmpty()) listIntake.map {
            medicationIntakeDao.insert(MedicationIntakeMapper.mapToEntity(it))
        }
    }

    private fun generateUniqueId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt()
        return "$timestamp$random"
    }

}