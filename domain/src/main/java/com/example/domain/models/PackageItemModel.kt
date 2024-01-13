package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PackageItemModel(
    val id: String,
    var intakesCount: Int = -1, // количество приемов в упаковке [calculate]
    var durationInDays: Int = -1, // количество дней [calculate]
    var startDate: Long = 0, // дата начала потребления упаковки [calculate]
    var endDate: Long = 0, // дата конца потребления упаковки [calculate]
    val expirationDate: Long, // срок годности упаковки [input]
    var quantityInPackage: Double, // количество лекарств в упаковке [input]
)