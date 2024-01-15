package uk.ac.aber.dcs.cs31620.gogym.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.ac.aber.dcs.cs31620.gogym.datasource.GoGymRepository
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayOfWeek
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import java.time.LocalDate

/**
 * Data View model
 * Gives the access to all Dao interfaces
 * It may not be the cleanest approach but I find it easier to work with
 */
class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GoGymRepository = GoGymRepository(application)

    var days: LiveData<List<Day>> = loadDays()
    var workouts: LiveData<List<Workout>> = loadWorkouts()
    var exercises: LiveData<List<Exercise>> = loadExercises()

    fun getTodayWorkout(): LiveData<Workout?> {
        return repository.getWorkoutByDayOfWeek(getTodayDayOfWeek())
    }

    fun getDaysWorkout(day: Day): Workout = repository.getWorkoutNonLive(day.workoutID!!)!!

    fun getNonLiveDayByID(dayID: Long): Day = repository.getDayByIDNonLive(dayID)

    fun getWorkoutExercises(workout: Workout): LiveData<List<Exercise>> =
        repository.getWorkoutExercises(workout)

    /**
     * inner lambda takes care of the durations mismatch
     */
    fun deleteExercise(exercise: Exercise) {

        val allWorkouts = workouts.value
        val exerciseID = exercise.id

        allWorkouts?.let { workoutsList ->
            for (workout in workoutsList) {
                if (workout.exercisesIDs.contains(exerciseID)) {
                    workout.exercisesIDs.forEach {
                        if (it == exerciseID)
                            workout.totalDuration = workout.totalDuration.minus(exercise.duration)
                    }
                    workout.exercisesIDs = workout.exercisesIDs.filter { it != exerciseID }

                    updateWorkout(workout)
                }
            }
        }
        repository.deleteExercise(exercise)
    }

    fun insertExercise(exercise: Exercise) = repository.insertExercise(exercise)

    /**
     * inner lambda takes care of the durations mismatch
     */
    fun updateExercise(exercise: Exercise) {
        val allWorkouts = workouts.value
        val exerciseID = exercise.id

        allWorkouts?.let { workoutsList ->
            for (workout in workoutsList) {
                if (workout.exercisesIDs.contains(exerciseID)) {
                    workout.exercisesIDs.forEach {
                        if (it == exerciseID) {
                            val oldExercise = repository.getExerciseNonLive(exerciseID)
                            oldExercise?.let {
                                workout.totalDuration =
                                    workout.totalDuration.minus(oldExercise.duration)
                                        .plus(exercise.duration)
                            }
                        }
                    }
                    updateWorkout(workout)
                }
            }
        }
        repository.updateExercise(exercise)
    }

    /**
     * inner lambda prevents the deletion of the only scheduled workout
     */
    fun deleteWorkout(workout: Workout): Boolean {
        val allDays = repository.getAllDaysNonLive()

        allDays.let { daysList ->
            val daysWithWorkout =
                daysList.filter {
                    it?.let { it.workoutID != workout.id && it.workoutID != null } ?: false
                }

            if (daysWithWorkout.isNotEmpty()) {
                repository.deleteWorkout(workout)
                return true
            }
        }
        return false
    }

    fun insertWorkout(workout: Workout) = repository.insertWorkout(workout)

    fun updateWorkout(workout: Workout) = repository.updateWorkout(workout)

    fun updateDay(day: Day): Boolean {
        if (day.workoutID == null) {
            val allDays = repository.getAllDaysNonLive()
            allDays.let { daysList ->
                val daysWithWorkout =
                    daysList.filter {
                        it?.let { day.id != it.id && it.workoutID != null } ?: false
                    }

                if (daysWithWorkout.isEmpty()) {
                    return false
                }
            }
        }
        repository.updateDay(day)
        return true
    }


    fun getWorkoutByID(workoutID: Long): LiveData<Workout> =
        repository.getWorkout(workoutID)

    fun getNonLiveWorkoutByID(workoutID: Long) =
        repository.getWorkoutNonLive(workoutID)

    private fun getTodayDayOfWeek() = DayOfWeek.valueOf(LocalDate.now().dayOfWeek.toString())

    private fun loadDays(): LiveData<List<Day>> {
        return repository.getAllDays()
    }

    private fun loadWorkouts(): LiveData<List<Workout>> {
        return repository.getAllWorkouts()
    }

    private fun loadExercises(): LiveData<List<Exercise>> {
        return repository.getAllExercises()
    }
}
