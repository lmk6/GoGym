package uk.ac.aber.dcs.cs31620.gogym.model.workout

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleWorkout(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
    fun getWorkoutWithID(id: Long): LiveData<Workout>

    @Query("""SELECT * FROM workouts WHERE id IN (:ids)""")
    fun getWorkoutsWithIDs(ids: List<Long>): LiveData<List<Workout>>

    @Query("SELECT last_insert_rowid()")
    fun getLastInsertedID(): Long

    @Query("SELECT * FROM workouts WHERE id = :workoutID")
    fun getWorkoutByID(workoutID: Long): Workout

}