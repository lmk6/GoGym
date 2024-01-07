package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import java.lang.reflect.ParameterizedType

object ExercisesListConverter {
    private val moshi = Moshi.Builder().build()
    private val listMyData : ParameterizedType = Types.newParameterizedType(List::class.java, Exercise::class.java)
    private val jsonAdapter: JsonAdapter<List<Exercise>> = moshi.adapter(listMyData)
    @TypeConverter
    @JvmStatic
    fun exercisesToJsonString(exercises: List<Exercise>): String {
        return jsonAdapter.toJson(exercises)
    }

    @TypeConverter
    @JvmStatic
    fun jsonStringToExercises(jsonString: String): List<Exercise> {
        return jsonString.let { jsonAdapter.fromJson(jsonString)!! }
    }

}
