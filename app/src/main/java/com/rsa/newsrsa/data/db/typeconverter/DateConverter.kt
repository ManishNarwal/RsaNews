package com.rsa.newsrsa.data.db.typeconverter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateConverter {
    @TypeConverter
    fun toDate(date: String?): Date? {
        return if (date == null) null else {
            SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(date)
        }
    }

    @TypeConverter
    fun fromDate(date: Date): String? {
        return SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(date)
    }
}