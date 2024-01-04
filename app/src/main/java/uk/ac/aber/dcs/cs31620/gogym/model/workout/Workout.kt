package uk.ac.aber.dcs.cs31620.gogym.model.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise

@Entity(tableName = "workouts")
data class Workout(
    var id: Int = 0,
    var name: String,
    var exercises: List<Exercise>,
    @ColumnInfo(name = "image_path")
    var imagePath: String = ""
)