package com.example.medstime.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

/**### Функция *getCurrentDate()* возвращает текущую дату в виде строки формата **"dd.MM.yyyy"** */
fun getCurrentDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return LocalDate.now().format(formatter)
}

/**### Функция *generateStringId()* возвращает *String* - уникальный *ID* */
fun generateStringId() = UUID.randomUUID().toString()