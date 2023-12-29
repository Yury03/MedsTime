package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PackageItemModel(
    val id: String,
    var intakesCount: Int = -1,
    var endDate: Long = 0,
    val expirationDate: Long,
)