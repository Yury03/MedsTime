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
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

class AdditionalContractImpl(context: Context) : Repository.AdditionContract {
    private companion object {
        const val DEFAULT_NUMBER_DAYS_GENERATE = 14
        //по дефолту генерируем приемы только на 14 дней
    }

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
        generateMedicationIntakeModels(medicationModel).map {
            medicationIntakeDao.insert(MedicationIntakeMapper.mapToEntity(it))
        }
    }


    private fun generateMedicationIntakeModels(model: MedicationModel):
            List<MedicationIntakeModel> {
        val days = getNumberDays(model)

        return when (model.frequency) {
            MedicationModel.Frequency.DAILY -> generateModelsForDaily(model, days)
            MedicationModel.Frequency.EVERY_OTHER_DAY -> generateModelsForEveryOtherDay(model, days)
            MedicationModel.Frequency.SELECTED_DAYS -> generateModelsForSelectedDays(model, days)
        }
    }

    private fun getNumberDays(model: MedicationModel): Int {
        return when (model.trackType) {
            MedicationModel.TrackType.STOCK_OF_MEDICINE -> DEFAULT_NUMBER_DAYS_GENERATE //TODO
            MedicationModel.TrackType.DATE -> TimeUnit.MILLISECONDS
                .toDays(model.endDate!!.time - model.startDate.time).toInt()

            MedicationModel.TrackType.NUMBER_OF_DAYS -> model.numberOfDays!!.toInt()
            MedicationModel.TrackType.NONE -> DEFAULT_NUMBER_DAYS_GENERATE
        }
    }

    /**Функция генерирует список приемов на *days* дней с частотой **DAILY**.
     * (0 until days).flatMap используется как замена создания листа и перебора через for со
     * счетчиком.*/
    private fun generateModelsForDaily(model: MedicationModel, days: Int):
            List<MedicationIntakeModel> {
        return (0 until days).flatMap { dayIndex ->
            val localDate = getDateForDay(model.startDate, dayIndex)
            model.intakeTimes.map { intakeTime ->
                createMedicationIntakeModel(model, intakeTime, localDate)
            }
        }
    }

    /**Функция генерирует список приемов на *days* дней с частотой **EVERY_OTHER_DAY**.
     * (0 until days).flatMap используется как замена создания листа и перебора через for со
     * счетчиком. Каждый второй проход в коллекцию добавляется *emptyList()*, чтобы обеспечить
     * нужную частоту дат.*/
    private fun generateModelsForEveryOtherDay(model: MedicationModel, days: Int):
            List<MedicationIntakeModel> {
        return (0 until days).flatMap { dayIndex ->
            if (dayIndex % 2 == 0) {
                val localDate = getDateForDay(model.startDate, dayIndex)
                model.intakeTimes.map { intakeTime ->
                    createMedicationIntakeModel(model, intakeTime, localDate)
                }
            } else {
                emptyList()
            }
        }
    }

    /**Функция генерирует список приемов на *days* дней с частотой **SELECTED_DAYS**.
     * (0 until days).flatMap используется как замена создания листа и перебора через for со
     * счетчиком. Переменная *selectedDays* содержит массив с числами от 1 до 7 с понедельника
     * по воскресенье.*/
    private fun generateModelsForSelectedDays(model: MedicationModel, days: Int):
            List<MedicationIntakeModel> {
        val selectedDays = model.selectedDays ?: emptyList()//todo model.selectedDays!!
        return (0 until days).flatMap { dayIndex ->
            val localDate = getDateForDay(model.startDate, dayIndex)
            if (selectedDays.contains(localDate.dayOfWeek.value)) {
                model.intakeTimes.map { intakeTime ->
                    createMedicationIntakeModel(model, intakeTime, localDate)
                }
            } else {
                emptyList()
            }
        }
    }

    private fun getDateForDay(startDate: Date, dayIndex: Int): LocalDate {
        val dateIntake = Date(startDate.time + TimeUnit.DAYS.toMillis(dayIndex.toLong()))
        return dateIntake.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun createMedicationIntakeModel(
        model: MedicationModel,
        intakeTime: MedicationModel.Time,
        localDate: LocalDate
    ): MedicationIntakeModel {
        return MedicationIntakeModel(
            id = generateUniqueId(),
            name = model.name,
            dosage = model.dosage,
            dosageUnit = model.dosageUnit,
            isTaken = false,
            reminderTime = model.reminderTime,
            medicationId = model.id,
            intakeTime = MedicationIntakeModel.Time(intakeTime.hour, intakeTime.minute),
            intakeDate = MedicationIntakeModel.Date(localDate.dayOfMonth, localDate.monthValue),
            intakeType = when (model.intakeType) {
                MedicationModel.IntakeType.NONE -> MedicationIntakeModel.IntakeType.NONE
                MedicationModel.IntakeType.AFTER_MEAL -> MedicationIntakeModel.IntakeType.AFTER_MEAL
                MedicationModel.IntakeType.BEFORE_MEAL -> MedicationIntakeModel.IntakeType.BEFORE_MEAL
                MedicationModel.IntakeType.DURING_MEAL -> MedicationIntakeModel.IntakeType.DURING_MEAL
            }
        )
    }

    private fun generateUniqueId() = UUID.randomUUID().toString()


}