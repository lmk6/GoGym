package uk.ac.aber.dcs.cs31620.gogym.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.LocalDateConverter
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayOfWeek
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import java.time.LocalDate

class GoGymRepository(application: Application) {
    private val exerciseDao = GoGymRoomDatabase.getDatabase(application)!!.exerciseDao()
    private val workoutDao = GoGymRoomDatabase.getDatabase(application)!!.workoutDao()
    private val dayDao = GoGymRoomDatabase.getDatabase(application)!!.dayDao()

    fun insertExercise(exercise: Exercise) {
        exerciseDao.insertSingleExercise(exercise)
    }

    fun insertMultipleExercises(exercises: List<Exercise>) {
        exerciseDao.insertMultipleExercises(exercises)
    }

    fun getAllExercises() = exerciseDao.getAllExercises()

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    fun getDays() = dayDao.getAllDays()

//    fun getToday(): LocalDate? {
//        val today = LocalDate.now()
//        return dayDao.getToday(today)
//    }

    fun getTodayWorkout() =
        dayDao.getWorkoutForToday(DayOfWeek.valueOf(LocalDate.now().dayOfWeek.toString()))

    fun getWorkoutExercises(workout: Workout) = exerciseDao.getExercisesWithIDs(workout.exercisesIDs)

    fun getWorkout(workoutID: Long) = workoutDao.getWorkoutByID(workoutID)

}