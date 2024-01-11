package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout

object WorkoutConverter {
    private val type = object : TypeToken<Workout>(){}.type

    @TypeConverter
    @JvmStatic
    fun workoutToJson(workout: Workout?): String? {
        return Gson().toJson(workout, type)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToWorkout(jsonString: String?): Workout? {
        return Gson().fromJson(jsonString, type)
    }
}