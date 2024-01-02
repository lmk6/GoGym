package uk.ac.aber.dcs.cs31620.gogym.model

data class Workout(
    var id: Int = 0,
    var name: String = "",
    var exercises: List<Exercise> = listOf(),
    var imagePath: String = ""
)