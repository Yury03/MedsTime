package com.example.data.utils

import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.round

/**### Функция *Int.daysToMilliseconds() конвертирует дни в миллисекунды* */
fun Int.daysToMilliseconds(): Long {
    return this.toLong() * 24 * 60 * 60 * 1000
}

/**### Функция *Long.calculateDayDifference() возвращает модуль разницы дат в днях* */
fun Long.calculateDayDifference(time: Long): Int {
    val differenceInMillis = abs(this - time)
    return TimeUnit.MILLISECONDS.toDays(differenceInMillis).toInt()
}

/**### Функция *Long.millisecondsToDays() конвертирует миллисекунды в дни* */
fun Long.millisecondsToDays(): Int {
    return (this / (24 * 60 * 60 * 1000)).toInt()
}

/**### Функция *Double.customRound()* округляет Double с заданным шагом*/
fun Double.roundWithStep(step: Double): Double {
    return round(this / step) * step
}
