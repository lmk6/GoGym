package uk.ac.aber.dcs.cs31620.gogym.model.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.ac.aber.dcs.cs31620.gogym.datasource.GoGymRepository
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout

class DataViewModel(application: Application): AndroidViewModel(application) {
    private val repository: GoGymRepository = GoGymRepository(application)

    var days: LiveData<List<Day>> = loadDays()
    var workouts: LiveData<List<Workout>> = loadWorkouts()

    private fun loadDays(): LiveData<List<Day>> {
        return repository.getDays()
    }

    private fun loadWorkouts(): LiveData<List<Workout>> {
        return repository.getAllWorkouts()
    }
}