package uk.ac.aber.dcs.cs31620.gogym.model.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.ac.aber.dcs.cs31620.gogym.datasource.GoGymRepository
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import java.time.Duration

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GoGymRepository = GoGymRepository(application)

    var days: LiveData<List<Day>> = loadDays()
    var workouts: LiveData<List<Workout>> = loadWorkouts()
    var exercises: LiveData<List<Exercise>> = loadExercises()

    fun getTodayWorkout(): LiveData<Workout?> = repository.getTodayWorkout()

    fun getDaysWorkout(day: Day): Workout = repository.getWorkoutNonLive(day.workoutID!!)

    fun getWorkoutExercises(workout: Workout): LiveData<List<Exercise>> =
        repository.getWorkoutExercises(workout)

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

    fun updateWorkout(workout: Workout) = repository.updateWorkout(workout)

    fun getWorkoutByID(workoutID: Long): LiveData<Workout> =
        repository.getWorkout(workoutID)

    private fun loadDays(): LiveData<List<Day>> {
        return repository.getDays()
    }

    private fun loadWorkouts(): LiveData<List<Workout>> {
        return repository.getAllWorkouts()
    }

    private fun loadExercises(): LiveData<List<Exercise>> {
        return repository.getAllExercises()
    }
}
