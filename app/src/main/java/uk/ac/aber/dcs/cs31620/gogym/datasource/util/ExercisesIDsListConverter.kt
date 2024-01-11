package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise

object  ExercisesIDsListConverter {
    private val type = object : TypeToken<List<Long>>(){}.type
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
