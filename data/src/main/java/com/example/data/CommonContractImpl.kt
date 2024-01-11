package com.example.data

import android.content.Context
import android.util.Log
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.mappers.MedicationMapper
import com.example.data.mappers.ReminderMapper
import com.example.data.room.MedicationDatabase
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.MedsTrackDatabase
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.MedicationDao
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.dao.MedsTrackDao
import com.example.data.room.dao.ReminderDao
import com.example.data.utils.calculateDayDifference
import com.example.data.utils.daysToMilliseconds
import com.example.data.utils.generateStringId
import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.ReminderModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.time.Duration.Companion.minutes

/**Класс реализует общие юзкейсы. Используются MedicationDatabase, MedicationIntakeDatabase, ReminderDatabase.
 * - **remove medication model** удаляет модель Medication и все связанные с ней данные(уведомления, модели приемов);
 * - **save new medication** добавляет модель Medication в базу данных и генерирует модели MedicationIntake и Reminder на **DEFAULT_NUMBER_DAYS_GENERATE** дней, а затем добавляет их в базу данных **MedicationIntakeDatabase** и **ReminderDatabase**;
 * - **replace medication model** заменяет medication model, использует удаление по id, и сохраненение новой модели Medication;
 * - **get medication model by id** получает модель Medication по id.
 * */
class CommonContractImpl(private val context: Context) : Repository.CommonContract {
    private companion object {
        //по дефолту генерируем приемы только на 14 дней
        const val DEFAULT_NUMBER_DAYS_GENERATE = 14
        const val LOG_TAG = "Common contract implementation"
    }

    private val medicationDatabase: MedicationDatabase by lazy {
        MedicationDatabase.getDatabase(context)
    }
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }
    private val reminderDatabase: ReminderDatabase by lazy {
        ReminderDatabase.getDatabase(context)
    }
    private val medsTrackDatabase: MedsTrackDatabase by lazy {
        MedsTrackDatabase.getDatabase(context)
    }
    private val medicationDao: MedicationDao by lazy { medicationDatabase.medicationDao() }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }
    private val reminderDao: ReminderDao by lazy { reminderDatabase.reminderDao() }
    private val medsTrackDao: MedsTrackDao by lazy { medsTrackDatabase.medsTrackDao() }

    /**Функция готовит medicationModel (вычисляет все нужные данные), маппит ее в val (medicationEntity, medsTrackEntity),
     *  затем эти entity добавляются в соответствующие таблицы базы данных,
     *  затем происходит получение и сохранение сгенерированных моделей приемов и уведомлений*/
    override suspend fun saveNewMedication(medicationModel: MedicationModel) {
        fillTrackModel(medicationModel)
        Log.i(LOG_TAG, medicationModel.toString())
        val (medicationEntity, medsTrackEntity) =
            MedicationMapper.mapToEntity(medicationModel)
        medicationDao.insert(medicationEntity)
        medsTrackDao.insert(medsTrackEntity)
        val medicationIntakeList = generateMedicationIntakeModels(medicationModel)
        val reminderList = generateReminderModels(medicationIntakeList, medicationModel)
        reminderList.map {
            reminderDao.insert(ReminderMapper.mapToEntity(it))
        }
        medicationIntakeList.map {
            medicationIntakeDao.insert(MedicationIntakeMapper.mapToEntity(it))
        }
    }

    /**Функция fillTrackModel() вычисляет все необходимые данные для trackModel*/
    private fun fillTrackModel(medicationModel: MedicationModel) {
        with(medicationModel.trackModel) {
            when (medicationModel.trackModel.trackType) {
                MedsTrackModel.TrackType.PACKAGES_TRACK -> {
                    stockOfMedicine = packageItems.sumOf {
                        it.quantityInPackage
                    }
                    fillPackageItemModels(medicationModel)
                    calculateRemainingDoses(medicationModel)
                    totalDays = packageItems.sumOf {
                        it.durationInDays
                    }
//todo                    calculateTotalDays(medicationModel)[?]
                }

                MedsTrackModel.TrackType.STOCK_OF_MEDICINE -> {
                    calculateRemainingDoses(medicationModel)
                    calculateTotalDays(medicationModel)
                }

                MedsTrackModel.TrackType.DATE -> {
                    totalDays = medicationModel.startDate.calculateDayDifference(endDate)
                    calculateStockWithTotalDays(medicationModel)
                    calculateRemainingDoses(medicationModel)
                }

                MedsTrackModel.TrackType.NUMBER_OF_DAYS -> {
                    totalDays = numberOfDays
                    calculateStockWithTotalDays(medicationModel)
                    calculateRemainingDoses(medicationModel)
                }

                MedsTrackModel.TrackType.NONE -> {
                    totalDays = DEFAULT_NUMBER_DAYS_GENERATE
                }
            }
        }

    }

    private fun calculateStockWithTotalDays(medication: MedicationModel) {
        var stockOfMedicine = -1.0
        val dailyDosage = getDailyDosage(medication)
        var totalDays = medication.trackModel.totalDays
        when (medication.frequency) {
            MedicationModel.Frequency.DAILY -> {
                while (totalDays > 0) {
                    stockOfMedicine += dailyDosage
                    totalDays--
                }
            }

            MedicationModel.Frequency.EVERY_OTHER_DAY -> {
                while (totalDays > 0) {
                    if (totalDays % 2 == 0) {
                        stockOfMedicine += dailyDosage
                        totalDays--
                    }
                }
            }

            MedicationModel.Frequency.SELECTED_DAYS -> {
                val frequencyArray = getFrequencyArray(medication.selectedDays!!)
                var currentDay = getDayOfWeek(medication.startDate)
                while (totalDays > 0) {
                    if (frequencyArray[currentDay]) {
                        stockOfMedicine += dailyDosage
                        totalDays--
                    }
                    currentDay = (currentDay + 1) % 7
                }
            }

        }
        medication.trackModel.stockOfMedicine = stockOfMedicine
    }

    /**### Функция calculateTotalDays() вычисляет количество дней на протяжении которых пользователь будет принимать лекарства. ***ДЛЯ ВЫЗОВА НЕОБХОДИМО ПОЛЕ trackModel.stockOfMedicine*** */
    private fun calculateTotalDays(medication: MedicationModel) {
        if (medication.trackModel.stockOfMedicine >= 0) return//todo?
        with(medication) {
            val dailyDosage = getDailyDosage(this)
            var currentDay = getDayOfWeek(startDate) //от 0 до 6
            var totalDays = 0
            var stock = trackModel.stockOfMedicine
            when (frequency) {
                MedicationModel.Frequency.DAILY -> {
                    while (trackModel.stockOfMedicine - dailyDosage >= 0) {
                        stock -= dailyDosage
                        totalDays++
                    }
                }

                MedicationModel.Frequency.EVERY_OTHER_DAY -> {
                    while (trackModel.stockOfMedicine - dailyDosage >= 0) {
                        if (totalDays % 2 == 0) {
                            stock -= dailyDosage
                        }
                        currentDay = (currentDay + 1) % 7
                        totalDays++
                    }
                }

                MedicationModel.Frequency.SELECTED_DAYS -> {
                    val frequencyArray = getFrequencyArray(selectedDays!!)
                    while (trackModel.stockOfMedicine - dailyDosage >= 0) {
                        if (frequencyArray[currentDay]) {
                            stock -= dailyDosage
                        }
                        currentDay = (currentDay + 1) % 7
                        totalDays++
                    }
                }
            }
            trackModel.totalDays = totalDays
        }
    }

    /**Функция getDayOfWeek() возвращает день недели в формате Int.
     * - Monday - 0;
     * - Sunday - 6.*/
    private fun getDayOfWeek(startDate: Long): Int {
        return (Calendar.getInstance().apply {
            timeInMillis = startDate
        }.get(Calendar.DAY_OF_WEEK) + 5) % 7
    }

    private fun getFrequencyArray(selectedDays: List<Int>): List<Boolean> = List(7) { index ->
        index in selectedDays
    }

    private fun calculateRemainingDoses(medication: MedicationModel) {
        with(medication) {
            trackModel.remainingDoses =
                floor(trackModel.stockOfMedicine / dosage).toInt()
        }
    }

    //todo добавить генерацию recommendedPurchaseDate, startDate и учесть expirationDate
    /**Функция editPackageItemModels вычисляет количество приемов, на сколько дней хватит упаковки
     *  и дату начала и окончания упаковки */
    private fun fillPackageItemModels(medication: MedicationModel) {
        val dailyDosage = getDailyDosage(medication)
        medication.trackModel.packageItems.forEachIndexed { i, packageItem ->
            val quantityInPackage = packageItem.quantityInPackage
            val dosesInPackage = floor(quantityInPackage / medication.dosage).toInt()
            val durationInDays =
                floor(packageItem.quantityInPackage / dailyDosage).toInt()

            packageItem.intakesCount = dosesInPackage
            packageItem.durationInDays = durationInDays

            if (i > 0) {
                val previousPackageItem = medication.trackModel.packageItems[i - 1]
                packageItem.startDate = previousPackageItem.endDate
                packageItem.endDate = packageItem.startDate + durationInDays.daysToMilliseconds()
            } else {
                packageItem.startDate = medication.startDate
                packageItem.endDate = packageItem.startDate + durationInDays.daysToMilliseconds()
            }
        }
    }


    private fun generateMedicationIntakeModels(model: MedicationModel): List<MedicationIntakeModel> {
        return when (model.frequency) {
            MedicationModel.Frequency.DAILY -> generateModelsForDaily(model)
            MedicationModel.Frequency.EVERY_OTHER_DAY -> generateModelsForEveryOtherDay(model)
            MedicationModel.Frequency.SELECTED_DAYS -> generateModelsForSelectedDays(model)
        }
    }

    private fun getDailyDosage(medicationModel: MedicationModel) =
        medicationModel.dosage * medicationModel.intakeTimes.size


    /**Функция генерирует список приемов на *days* дней с частотой **DAILY**.
     * (0 until days).flatMap используется как замена создания листа и перебора через for со
     * счетчиком.*/
    private fun generateModelsForDaily(model: MedicationModel): List<MedicationIntakeModel> {
        return (0 until model.trackModel.totalDays).flatMap { dayIndex ->
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
    private fun generateModelsForEveryOtherDay(model: MedicationModel): List<MedicationIntakeModel> {
        return (0 until model.trackModel.totalDays).flatMap { dayIndex ->
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
    private fun generateModelsForSelectedDays(model: MedicationModel): List<MedicationIntakeModel> {
        val selectedDays = model.selectedDays!!
        return (0 until model.trackModel.totalDays).flatMap { dayIndex ->
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

    private fun generateReminderModels(
        intakeList: List<MedicationIntakeModel>, medicationModel: MedicationModel
    ): List<ReminderModel> {
        val reminderList = mutableListOf<ReminderModel>()
        val type = if (medicationModel.useBanner) {
            ReminderModel.Type.BANNER
        } else {
            ReminderModel.Type.PUSH_NOTIFICATION
        }
        for (intake in intakeList) {
            reminderList.add(
                ReminderModel(
                    id = generateStringId(),
                    medicationIntakeId = intake.id,
                    type = type,
                    status = ReminderModel.Status.NONE,
                    timeShow = getTime(intake.intakeTime, intake.intakeDate, intake.reminderTime)
                )
            )
        }
        return reminderList
    }

    private fun getTime(
        time: MedicationIntakeModel.Time, date: MedicationIntakeModel.Date, reminderTime: Int
    ): Long {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, date.year)
            set(Calendar.MONTH, date.month - 1)
            set(Calendar.DAY_OF_MONTH, date.day)
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
        }.timeInMillis - reminderTime.minutes.inWholeMilliseconds
    }

    //todo test getDateForDay
    private fun getDateForDay(startDate: Long, dayIndex: Int): LocalDate {
        val dateIntake = Date(startDate + TimeUnit.DAYS.toMillis(dayIndex.toLong()))
        return dateIntake.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun createMedicationIntakeModel(
        model: MedicationModel, intakeTime: MedicationModel.Time, localDate: LocalDate
    ): MedicationIntakeModel {
        return MedicationIntakeModel(
            id = generateStringId(),
            name = model.name,
            dosage = model.dosage,
            dosageUnit = model.dosageUnit,
            reminderTime = model.reminderTime,
            medicationId = model.id,
            intakeTime = MedicationIntakeModel.Time(intakeTime.hour, intakeTime.minute),
            intakeDate = MedicationIntakeModel.Date(
                localDate.dayOfMonth, localDate.monthValue, localDate.year
            ),
            intakeType = when (model.intakeType) {
                MedicationModel.IntakeType.NONE -> MedicationIntakeModel.IntakeType.NONE
                MedicationModel.IntakeType.AFTER_MEAL -> MedicationIntakeModel.IntakeType.AFTER_MEAL
                MedicationModel.IntakeType.BEFORE_MEAL -> MedicationIntakeModel.IntakeType.BEFORE_MEAL
                MedicationModel.IntakeType.DURING_MEAL -> MedicationIntakeModel.IntakeType.DURING_MEAL
            }
        )
    }


    override suspend fun getMedicationById(id: String): MedicationModel {
        val medicationEntity = medicationDao.getById(id)
        val medsTrackEntity = medsTrackDao.getById(medicationEntity.medsTrackModelId)
        return MedicationMapper.mapToModel(medicationEntity, medsTrackEntity)
    }

    override suspend fun replaceMedicationModel(medicationModel: MedicationModel) {
        removeMedicationModel(medicationModel.id)
        saveNewMedication(medicationModel)
    }

    /**Метод **removeMedicationModel**
     * - получает все *приемы лекарств*
     * - меняет статус всех напоминаний на DEPRECATED по *id приема лекарств*
     * - удаляет все *приемы лекарств по id модели Medication*
     * - удаляет модель Medication*/
    override suspend fun removeMedicationModel(medicationModelId: String) {
        val status = ReminderModel.Status.DEPRECATED.toString()//todo?
        medicationIntakeDao.getByMedicationModelId(medicationModelId).forEach {
            reminderDao.updateStatusByMedicationIntakeId(it.id, status)
        }
        medicationIntakeDao.deleteByMedicationModelId(medicationModelId)
        medicationDao.deleteById(medicationModelId)
    }
}