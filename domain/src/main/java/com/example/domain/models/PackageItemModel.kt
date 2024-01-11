package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PackageItemModel(
    val id: String,
    var intakesCount: Int = -1, // количество приемов в упаковке
    var durationInDays: Int = -1, // количество дней
    var startDate: Long = 0, // дата начала употребления упаковки
    var endDate: Long = 0, // дата конца употребления упаковки
    val expirationDate: Long, // срок годности упаковки
    val quantityInPackage: Double, // количество лекарств в упаковке
)