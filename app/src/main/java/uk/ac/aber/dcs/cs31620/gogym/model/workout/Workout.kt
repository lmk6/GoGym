package uk.ac.aber.dcs.cs31620.gogym.model.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

/**
 * Workout Entity
 * @property getFormattedDuration returns the duration formatted in HHh MMmin SSs format
 * @param exercisesIDs Keeps the exercises' IDs to keep the order and occurrences.
 */
@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    @ColumnInfo(name = "exercises_ids")
    var exercisesIDs: List<Long>,
    var totalDuration: Duration = Duration.ZERO,
    @ColumnInfo(name = "image_path")
    var imagePath: String = ""
) {
    fun getFormattedDuration(): String {
        val hours = totalDuration.toHours()
        val minutes = (totalDuration.toMinutes() % 60)
        val seconds = (totalDuration.seconds % 60)

        val formattedParts = mutableListOf<String>()

        if (hours > 0) {
            formattedParts.add(String.format("%dh", hours))
        }
        if (minutes > 0) {
            formattedParts.add(String.format("%dmin", minutes))
        }
        if (seconds > 0 || formattedParts.isEmpty()) {
            formattedParts.add(String.format("%ds", seconds))
        }

        return formattedParts.joinToString(" ")
    }
}