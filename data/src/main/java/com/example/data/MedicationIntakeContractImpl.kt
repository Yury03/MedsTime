package com.example.data

import android.content.Context
import android.util.Log
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.dao.MedicationIntakeDao
import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**Класс реализует юзкейсы, связанные с приемами лекарств. Используется только MedicationIntakeDatabase.
 * - **get intake list** возвращает все модели приемов из MedicationIntakeDatabase;
 * - **change actual time intake** меняет фактическое время приема;
 * - **change medication intake is taken** меняет статус приема;
 * - **get medication intake model** получает модель MedicationIntake по id.*/
class MedicationIntakeContractImpl(context: Context) : Repository.MedicationIntakeContract {
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }

    companion object {
        const val LOG_TAG = "MedicationIntakeContractImpl"
    }

    override suspend fun getIntakeList(): Flow<List<MedicationIntakeModel>> {
        val medicationIntakeList = medicationIntakeDao.getAll()
        val flowListModel = medicationIntakeList.map { list ->
            list?.map {
                MedicationIntakeMapper.mapToModel(it)
            } ?: emptyList()
        }
        return flowListModel
    }


    override suspend fun changeActualTimeIntake(
        medicationIntakeId: String,
        newTime: MedicationIntakeModel.Time
    ) {
        medicationIntakeDao.updateActualIntakeTimeById(medicationIntakeId, newTime.toEntityString())
    }

    override suspend fun changeMedicationIntakeIsTaken(
        medicationIntakeId: String,
        newIsTaken: Boolean,
        actualIntakeTime: MedicationIntakeModel.Time?
    ) {
        Log.i(LOG_TAG, "changeMedicationIntakeIsTaken: $newIsTaken")
        medicationIntakeDao.updateIsTakenById(
            medicationIntakeId,
            newIsTaken,
            actualIntakeTime?.toEntityString()
        )
    }

    override suspend fun getMedicationIntakeModel(medicationIntakeId: String) =
        MedicationIntakeMapper.mapToModel(medicationIntakeDao.getById(medicationIntakeId))

}