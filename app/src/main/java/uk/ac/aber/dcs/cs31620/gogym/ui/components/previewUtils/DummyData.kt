package uk.ac.aber.dcs.cs31620.gogym.ui.components.previewUtils

import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayOfWeek
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import java.time.Duration

val dummyExercise = Exercise(
    name = "Regular Push Ups",
    numOfSets = 2,
    duration = Duration.ofMinutes(6),
    repsPerSet = 8,
    imagePath = "file:///android_asset/images/push_ups_img.jpg"
)

val dummyWorkout = Workout(
    name = "Push Ups",
    imagePath = "file:///android_asset/images/push_ups_img.jpg",
    exercisesIDs = listOf(0),
    totalDuration = Duration.ofMinutes(70).plusSeconds(20)
)

val dummyDay = Day(
    workoutID = 0,
    dayOfWeek = DayOfWeek.MONDAY
)