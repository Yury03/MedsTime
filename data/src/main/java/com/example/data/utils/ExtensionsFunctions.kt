package com.example.data.utils

import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**### Функция *Int.daysToMilliseconds() конвертирует дни в миллисекунды* */
fun Int.daysToMilliseconds(): Long {
    return this.toLong() * 24 * 60 * 60 * 1000
}

/**### Функция *Long.calculateDayDifference() возвращает модуль разницы дат в днях* */
fun Long.calculateDayDifference(time: Long): Int {
    val differenceInMillis = abs(this - time)
    return TimeUnit.MILLISECONDS.toDays(differenceInMillis).toInt()
}