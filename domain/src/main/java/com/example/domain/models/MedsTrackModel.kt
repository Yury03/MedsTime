package com.example.domain.models

data class MedsTrackModel(
    val id: String,
    val name: String,
    val endDate: Long,//конечная дата отслеживания
    val packageCounter: Int,//количество оставшихся упаковок
    val recommendedPurchaseDate: Long,
    val packageItems: List<PackageItemModel>,
)