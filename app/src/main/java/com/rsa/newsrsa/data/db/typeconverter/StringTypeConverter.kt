package com.rsa.newsrsa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections

class StringTypeConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<String> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }
    @TypeConverter
    fun listToString(someObjects: List<String>): String {
        return gson.toJson(someObjects)
    }
}