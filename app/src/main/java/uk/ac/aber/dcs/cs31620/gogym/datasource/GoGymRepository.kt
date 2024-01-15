package uk.ac.aber.dcs.cs31620.gogym.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayOfWeek
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout

/**
 * GoGym Repository.
 * All the function connecting the project with the database.
 */
class GoGymRepository(application: Application) {
    private val exerciseDao = GoGymRoomDatabase.getDatabase(application)!!.exerciseDao()
    private val workoutDao = GoGymRoomDatabase.getDatabase(application)!!.workoutDao()
    private val dayDao = GoGymRoomDatabase.getDatabase(application)!!.dayDao()

    fun insertExercise(exercise: Exercise) {
        exerciseDao.insertSingleExercise(exercise)
    }

    fun getAllExercises() = exerciseDao.getAllExercises()

    fun getExerciseNonLive(exerciseID: Long) = exerciseDao.getNonLiveExerciseByID(exerciseID)

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    fun updateDay(day: Day) = dayDao.updateDay(day)

    fun getAllDays() = dayDao.getAllDays()

    fun getAllDaysNonLive() = dayDao.getNonLiveAllDays()

    fun getDayByIDNonLive(dayID: Long) = dayDao.getNonLiveDayByID(dayID)

    fun getWorkoutByDayOfWeek(day: DayOfWeek) = workoutDao.getWorkoutForDay(day)

    fun getWorkoutExercises(workout: Workout) = exerciseDao.getExercisesByIDs(workout.exercisesIDs)

    fun getWorkout(workoutID: Long) = workoutDao.getWorkoutByID(workoutID)

    fun insertWorkout(workout: Workout) = workoutDao.insertSingleWorkout(workout)

    fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise)
    }
    fun updateExercise(updatedExercise: Exercise) = exerciseDao.updateExercise(updatedExercise)

    fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    fun updateWorkout(workout: Workout) {
        workoutDao.updateWorkout(workout)
    }

    fun getWorkoutNonLive(workoutID: Long) = workoutDao.getNonLiveWorkoutByID(workoutID)

}