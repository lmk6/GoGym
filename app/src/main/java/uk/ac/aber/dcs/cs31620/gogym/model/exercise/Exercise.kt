package uk.ac.aber.dcs.cs31620.gogym.model.exercise

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    @ColumnInfo(name = "num_of_sets")
    var numOfSets: Int,
    @ColumnInfo(name = "reps_per_set")
    var repsPerSet: Int,
    var duration: Duration = Duration.ofSeconds(0),
    @ColumnInfo(name = "weight_enabled")
    var weightEnabled: Boolean = false,
    @ColumnInfo(name = "weight_one")
    var weightOne: Float = 0f,
    @ColumnInfo(name = "weight_two")
    var weightTwo: Float = 0f,
    @ColumnInfo(name = "weight_three")
    var weightThree: Float = 0f,
    @ColumnInfo(name = "drop_sets_feature")
    var dropSetsFeatureEnabled: Boolean = false,
    @ColumnInfo(name = "image_path")
    var imagePath: String = ""
) {
    fun getFormattedDuration(): String {
        val hours = duration.toHours()
        val minutes = (duration.toMinutes() % 60)
        val seconds = (duration.seconds % 60)

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
