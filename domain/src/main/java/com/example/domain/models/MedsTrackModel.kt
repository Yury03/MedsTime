package com.example.domain.models

import java.io.Serializable

//todo @Serializable не работает
data class MedsTrackModel(
    var id: String = "",
    var name: String = "",
    var recommendedPurchaseDate: Long = 0,
    val packageItems: MutableList<PackageItemModel> = mutableListOf(),
    var packageCounter: Int = -1, // количество оставшихся упаковок
    var stockOfMedicine: Double = -1.0, // запас лекарств
    var numberOfDays: Int = -1, // количество дней курса приема
    var endDate: Long = -1, // конечная дата отслеживания
    var trackType: TrackType = TrackType.NONE,
) : Serializable {
    enum class TrackType {
        PACKAGES_TRACK,     // Отслеживание добавленных упаковок
        STOCK_OF_MEDICINE,  // Курс (количество лекарств)
        DATE,               // Дата
        NUMBER_OF_DAYS,     // Курс (количество дней)
        NONE,               // Не добавлять отслеживание
    }
}
