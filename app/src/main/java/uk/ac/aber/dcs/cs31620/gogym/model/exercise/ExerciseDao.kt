package uk.ac.aber.dcs.cs31620.gogym.model.exercise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDao {
    @Insert
    fun insertSingleExercise(exercise: Exercise)

    @Insert
    fun insertMultipleExercises(exerciseList: List<Exercise>)

    @Update
    fun updateExercise(exercise: Exercise)

    @Delete
    fun deleteExercise(exercise: Exercise)

    @Query("DELETE FROM exercises")
    fun deleteAll()

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("""SELECT * FROM exercises WHERE id = :id LIMIT 1""")
    fun getExerciseWithID(id: Int): LiveData<Exercise>

    @Query("""SELECT * FROM exercises WHERE id IN (:ids)""")
    fun getExercisesWithIDs(ids: List<Int>): LiveData<List<Exercise>>
}