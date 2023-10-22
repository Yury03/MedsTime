package com.example.data.mappers

import com.example.data.room.entity.MedsTrackEntity
import com.example.domain.models.MedsTrackModel

object MedsTrackMapper {
    fun mapToEntity(model: MedsTrackModel): MedsTrackEntity {
        return MedsTrackEntity(
            id = model.id,
            name = model.name,
            endDate = model.endDate,
            packageCounter = model.packageCounter,
            recommendedPurchaseDate = model.recommendedPurchaseDate,
        )
    }

    fun mapToModel(entity: MedsTrackEntity): MedsTrackModel {
        return MedsTrackModel(
            id = entity.id,
            name = entity.name,
            endDate = entity.endDate,
            packageCounter = entity.packageCounter,
            recommendedPurchaseDate = entity.recommendedPurchaseDate,
        )
    }
}