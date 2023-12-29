package com.example.medstime.ui.add_track

import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel

/** Класс **AddMedTrackState** частично состоит из данных,
 *  взятых из *AddMedState*, которые берутся из входящего **Bundle**.
 *  В **AddMedTrackState** содержится ***medName***, ***dosageUnit*** и ***trackModel***,
 *  которые можно редактировать с фрагмента **AddMedTrackFragment**.*/
data class AddMedTrackState(
    val medName: String = "", // поле ввода наименования лекарства
    val medTrack: MedsTrackModel = MedsTrackModel(), // модель отслеживания, берется из addMedState
    val actualPackageList: List<PackageItemModel> = emptyList(), // данные вводятся с помощью ui и берутся из addMedState, если medicationModel редактируется
    val expirationDate: Long = 0, // поле ввода срока годности
    val dosageUnit: String = "", // поле ввода единиц измерения
    val quantityInPackage: String = "", // поле ввода количества упаковок
    val errorCode: Int = VALID, // код ошибки
    val addMedStateJson: String = "", // json состояния AddMedFragment, существует после обработки аргументов, после всех изменений отправляется обратно в AddMedFragment
) {
    companion object ERROR {
        const val VALID = 0
        const val EXPIRATION_DATE_IS_EMPTY = 1
        const val EXPIRATION_DATE_TOO_SMALL = 2
        const val QUANTITY_IN_PACKAGE_IS_EMPTY = 3
    }
}