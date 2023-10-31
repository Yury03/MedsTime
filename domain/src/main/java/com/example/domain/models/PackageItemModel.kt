package com.example.domain.models

data class PackageItemModel(
    val id: String,
    val idMedsTrackModel: String,
    val intakesCount: Int,
    val endDate: Long,
    val expirationDate: Long,
)
