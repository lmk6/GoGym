package uk.ac.aber.dcs.cs31620.gogym.model.day

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Day Data Access object
 */
@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleDay(day: Day)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleDays(dayList: List<Day>)

    @Update
    fun updateDay(day: Day)

    @Delete
    fun deleteDay(day: Day)

    @Query("DELETE FROM days")
    fun deleteAll()

    @Query("SELECT * FROM days")
    fun getAllDays(): LiveData<List<Day>>

    @Query("SELECT * FROM days")
    fun getNonLiveAllDays(): List<Day?>

    @Query("SELECT * FROM days WHERE id = :dayID LIMIT 1")
    fun getNonLiveDayByID(dayID: Long): Day

    @Query("SELECT * FROM days where dayOfWeek = :day LIMIT 1")
    fun getNonLiveDayByDayOfWeek(day: DayOfWeek): Day
}