package com.example.domain.models

import java.io.Serializable

//todo @Serializable не работает
data class MedsTrackModel(
    var id: String = "",
    var name: String = "",
    val packageItems: MutableList<PackageItemModel> = mutableListOf(), // список упаковок
    var stockOfMedicine: Double = -1.0, // запас лекарств [у всех типов отслеживания, кроме NONE]
    var numberOfDays: Int = -1, // количество дней курса приема [только у NUMBER_OF_DAYS, остальные типы используют totalDays]//todo исправить(оставить только totalDays)
    var endDate: Long = -1, // конечная дата отслеживания
    var remainingDoses: Int = -1, // количество оставшихся приемов [у всех типов отслеживания, кроме NONE]
    var recommendedPurchaseDate: Long = 0, // рекомендованная дата покупки [только у PACKAGES_TRACK]
    var packageCounter: Int = -1, // количество оставшихся упаковок [только у PACKAGES_TRACK]
    var totalDays: Int = -1, // [У всех типов отслеживания]
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
