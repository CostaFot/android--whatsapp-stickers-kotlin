package com.feelsokman.database.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String): List<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {
        }.type
        return Gson().fromJson<ArrayList<String>>(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromArrayList(list: List<String>): String {
        return Gson().toJson(list)
    }
}
