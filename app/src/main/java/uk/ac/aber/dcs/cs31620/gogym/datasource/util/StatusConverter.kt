package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutStatus

object StatusConverter {
    @TypeConverter
    @JvmStatic
    fun toString(workoutStatus: WorkoutStatus) = workoutStatus.toString()

    @TypeConverter
    @JvmStatic
    fun toWorkoutStatus(workoutStatus: String) = WorkoutStatus.valueOf(workoutStatus)
}