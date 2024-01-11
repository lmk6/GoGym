package uk.ac.aber.dcs.cs31620.gogym.datasource.util

import androidx.room.TypeConverter
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayOfWeek
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutStatus

object DayOfWeekConverter {
    @TypeConverter
    @JvmStatic
    fun toString(dayOfWeek: DayOfWeek) = dayOfWeek.toString()

    @TypeConverter
    @JvmStatic
    fun toDayOfWeek(dayOfWeek: String) = DayOfWeek.valueOf(dayOfWeek)
}