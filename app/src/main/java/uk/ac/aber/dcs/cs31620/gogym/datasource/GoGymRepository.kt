package uk.ac.aber.dcs.cs31620.gogym.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise

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
}