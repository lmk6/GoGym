package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converts a list of exercises into a json string of their IDs
 * To keep the order and number of occurrences.
 */
object ExercisesIDsListConverter {
    private val type = object : TypeToken<List<Long>>() {}.type

    @TypeConverter
    @JvmStatic
    fun exercisesToJsonString(exercises: List<Long>): String {
        return Gson().toJson(exercises, type)
    }

    @TypeConverter
    @JvmStatic
    fun jsonStringToExercises(jsonString: String): List<Long> {
        return Gson().fromJson(jsonString, type)
    }

}
