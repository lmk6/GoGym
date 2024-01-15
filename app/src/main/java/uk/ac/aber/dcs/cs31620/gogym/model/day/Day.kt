package uk.ac.aber.dcs.cs31620.gogym.model.day

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout

/**
 * Day entity
 * Foreign key automates the deletion of a scheduled workout
 */
@Entity(tableName = "days",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workout_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Day(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var dayOfWeek: DayOfWeek,
    @ColumnInfo(name = "workout_id")
    var workoutID: Long? = null
)