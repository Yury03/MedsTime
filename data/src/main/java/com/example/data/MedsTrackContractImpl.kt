package com.example.data

import android.content.Context
import com.example.data.mappers.MedsTrackMapper
import com.example.data.room.MedsTrackDatabase
import com.example.data.room.dao.MedsTrackDao
import com.example.domain.Repository
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel

class MedsTrackContractImpl(context: Context) : Repository.MedsTrackContract {
    private val medsTrackDatabase: MedsTrackDatabase by lazy {
        MedsTrackDatabase.getDatabase(context)
    }
    private val medsTrackDao: MedsTrackDao by lazy { medsTrackDatabase.medsTrackDao() }
    override suspend fun getAllTracks(): List<MedsTrackModel> {
//        return medsTrackDao.getAll().map {
//            MedsTrackMapper.mapToModel(it)
//        }
//        return emptyList()
        val packageItemsStub = mutableListOf(
            PackageItemModel(
                id = "56435382",
                intakesCount = 343,
                endDate = 1683963600900,
                expirationDate = 1683963600900,
                durationInDays = 34,
                startDate = 1683962690900,
                quantityInPackage = 704.0,
            ),
            PackageItemModel(
                id = "58264353",
                intakesCount = 33,
                endDate = 1683963600900,
                expirationDate = 1683963600900,
                durationInDays = 34,
                startDate = 1683962690900,
                quantityInPackage = 704.0,
            ),
            PackageItemModel(
                id = "56482353",
                intakesCount = 33,
                endDate = 1683963600900,
                expirationDate = 1683963600900,
                durationInDays = 34,
                startDate = 1683962690900,
                quantityInPackage = 704.0,
            ),
            PackageItemModel(
                id = "56824353",
                intakesCount = 33,
                endDate = 1683963600900,
                expirationDate = 1683963600900,
                durationInDays = 34,
                startDate = 1683962690900,
                quantityInPackage = 704.0,
            ),
        )
        return listOf(
            MedsTrackModel(
                "111", "item 1",
                endDate = 1683844600900,
                packageCounter = 90,
                recommendedPurchaseDate = 1683963600900,
                packageItems = packageItemsStub,
            ),
            MedsTrackModel(
                "111", "item 2",
                endDate = 1683844600900,
                packageCounter = 24,
                recommendedPurchaseDate = 1683963600900,
                packageItems = packageItemsStub,
            ),
        )
    }

    override suspend fun getTrackById(medsTrackModelId: String): MedsTrackModel {
        return MedsTrackMapper.mapToModel(medsTrackDao.getById(medsTrackModelId))
    }

}