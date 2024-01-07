package uk.ac.aber.dcs.cs31620.gogym.model.exercise

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
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
    var dropSetsFeature: Boolean = false,
    @ColumnInfo(name = "image_path")
    var imagePath: String = ""
)
