package uk.ac.aber.dcs.cs31620.gogym.model

import java.time.LocalDate

data class Day(
    var date: LocalDate = LocalDate.now(),
    var workoutSession: Workout,
    var workoutStatus: WorkoutStatus
) {
    var name: String = date.dayOfWeek.toString()
}