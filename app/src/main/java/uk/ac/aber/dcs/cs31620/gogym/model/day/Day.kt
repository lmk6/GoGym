package uk.ac.aber.dcs.cs31620.gogym.model.day

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutStatus
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import java.time.LocalDate

@Entity(tableName = "days")
data class Day(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var date: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "workout_session")
    var workoutSession: Workout,
    @ColumnInfo(name = "workout_status")
    var workoutStatus: WorkoutStatus
) {
    var name: String = date.dayOfWeek.toString()
}