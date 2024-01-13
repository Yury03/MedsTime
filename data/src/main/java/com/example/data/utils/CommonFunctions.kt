package com.example.data.utils

import java.util.UUID

/**### Функция *generateStringId()* возвращает *String* - уникальный *ID*.
 * Используется для генерации моделей ***ReminderModel***, ***MedicationIntakeModel*** */
fun generateStringId() = UUID.randomUUID().toString()

