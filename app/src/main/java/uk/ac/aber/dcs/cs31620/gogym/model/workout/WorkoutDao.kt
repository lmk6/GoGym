package uk.ac.aber.dcs.cs31620.gogym.model.workout

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout

@Dao
interface WorkoutDao {
    @Insert
    fun insertSingleWorkout(workout: Workout)

    @Insert
    fun insertMultipleWorkouts(workoutList: List<Workout>)

    @Update
    fun updateWorkout(workout: Workout)

    @Delete
    fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workouts")
    fun deleteAll()

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("""SELECT * FROM workouts WHERE id = :id LIMIT 1""")
    fun getWorkoutWithID(id: Int): LiveData<Workout>

    @Query("""SELECT * FROM workouts WHERE id IN (:ids)""")
    fun getWorkoutsWithIDs(ids: List<Int>): LiveData<List<Workout>>

}