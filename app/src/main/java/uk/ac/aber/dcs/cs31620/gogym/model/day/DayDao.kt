package uk.ac.aber.dcs.cs31620.gogym.model.day

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DayDao {

    @Insert
    fun insertSingleDay(day: Day)

    @Insert
    fun insertMultipleDays(dayList: List<Day>)

    @Update
    fun updateDay(day: Day)

    @Delete
    fun deleteDay(day: Day)

    @Query("DELETE FROM days")
    fun deleteAll()

    @Query("SELECT * FROM days")
    fun getAllDays(): LiveData<List<Day>>
}