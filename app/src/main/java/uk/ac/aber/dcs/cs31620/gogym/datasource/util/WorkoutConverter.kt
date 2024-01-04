package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import java.lang.reflect.ParameterizedType

object WorkoutConverter {
    private val moshi = Moshi.Builder().build()
    private val listMyData : ParameterizedType = Types.newParameterizedType(Workout::class.java)
    private val jsonAdapter: JsonAdapter<Workout> = moshi.adapter(listMyData)

    @TypeConverter
    @JvmStatic
    fun workoutToJson(workout: Workout?): String? {
        return jsonAdapter.toJson(workout)
    }

    @TypeConverter
    @JvmStatic
    fun jsonToWorkout(jsonString: String?): Workout? {
        return jsonString?.let { jsonAdapter.fromJson(jsonString) }
    }
}