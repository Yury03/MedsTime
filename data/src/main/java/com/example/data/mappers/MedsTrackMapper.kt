package com.example.data.mappers

import com.example.data.room.entity.MedsTrackEntity
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MedsTrackMapper {

    fun mapToEntity(model: MedsTrackModel): MedsTrackEntity {
        return MedsTrackEntity(
            id = model.id,
            name = model.name,
            endDate = model.endDate,
            packageCounter = model.packageCounter,
            recommendedPurchaseDate = model.recommendedPurchaseDate,
            packageItems = mapListToString(model.packageItems),
            stockOfMedicine = model.stockOfMedicine,
            numberOfDays = model.numberOfDays,
            trackType = model.trackType.toString(),
        )
    }

    private fun mapListToString(packageItems: List<PackageItemModel>): String {
        val gson = Gson()
        return gson.toJson(packageItems)
    }

    fun mapToModel(entity: MedsTrackEntity): MedsTrackModel {
        return MedsTrackModel(
            id = entity.id,
            name = entity.name,
            endDate = entity.endDate,
            packageCounter = entity.packageCounter,
            recommendedPurchaseDate = entity.recommendedPurchaseDate,
            packageItems = mapStringToList(entity.packageItems),
            stockOfMedicine = entity.stockOfMedicine,
            numberOfDays = entity.numberOfDays,
            trackType = MedsTrackModel.TrackType.valueOf(entity.trackType),
        )
    }

    private fun mapStringToList(list: String): MutableList<PackageItemModel> {
        val gson = Gson()
        val type = object : TypeToken<List<PackageItemModel>>() {}.type
        return gson.fromJson(list, type)
    }

}