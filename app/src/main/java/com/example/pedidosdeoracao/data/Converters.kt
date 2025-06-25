package com.example.pedidosdeoracao.data

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it)
        }
    }

    @TypeConverter
    fun localDateTimeToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}