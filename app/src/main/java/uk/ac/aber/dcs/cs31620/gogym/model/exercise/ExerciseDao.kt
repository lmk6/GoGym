package uk.ac.aber.dcs.cs31620.gogym.model.exercise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleExercise(exercise: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
    fun getExerciseByID(id: Long): LiveData<Exercise>

    @Query("""SELECT * FROM exercises WHERE id = :id LIMIT 1""")
    fun getNonLiveExerciseByID(id: Long): Exercise?

    @Query("""SELECT * FROM exercises WHERE id IN (:ids)""")
    fun getExercisesByIDs(ids: List<Long>): LiveData<List<Exercise>>

    @Query("""SELECT * FROM exercises WHERE name = :name LIMIT 1""")
    fun getExerciseWithName(name: String): LiveData<Exercise>
}