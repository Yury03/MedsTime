package com.example.medstime.ui.utils

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

/**### Функция *String.toDate()* приводит строку вида dd.MM.yyyy к java.util.Date*/
fun String.toDate(): Date {
    val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val localDate = LocalDate.parse(this, format)
    val instant = localDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
    return Date.from(instant)
}

/**### Функция *Date.toDisplayString()* приводит java.util.Date к строке вида dd.MM.yyyy*/
@SuppressLint("SimpleDateFormat")
fun Date.toDisplayString(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    return formatter.format(this)
}

/**### Функция *Long.toDisplayString()* приводит Long к строке вида dd.MM.yyyy*/
fun Long.toDisplayString(): String {
    return Date(this).toDisplayString()
}

/**### Функция *Double.toDisplayString()* убирает незначащие нули и возвращает строку готовую для отображения*/
fun Double.toDisplayString() =
    if (this == this.toInt().toDouble()) {
        this.toInt().toString()
    } else {
        this.toString()
    }

/**### Функция *BottomNavigationView.show()* меняет поле visibility на VISIBLE*/
fun BottomNavigationView.show() {
    visibility = View.VISIBLE
}

/**### Функция *BottomNavigationView.hide()* меняет поле visibility на GONE*/
fun BottomNavigationView.hide() {
    visibility = View.GONE
}
