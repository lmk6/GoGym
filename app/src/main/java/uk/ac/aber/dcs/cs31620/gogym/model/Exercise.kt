package uk.ac.aber.dcs.cs31620.gogym.model

import androidx.room.PrimaryKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var numOfSets: Int = 0,
    var repsPerSet: Int = 0,
    var duration: Duration = 0.seconds,
    var weights: List<Float> = listOf(),
    var dropSetsFeature: Boolean = false,
    var imagePath: String = ""
)
