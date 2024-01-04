package uk.ac.aber.dcs.cs31620.gogym.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.ExercisesListConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.LocalDateConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.StatusConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.WorkoutConverter
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayDao
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.ExerciseDao
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutDao
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutStatus
import kotlin.time.Duration.Companion.minutes

@Database(entities = [Day::class, Workout::class, Exercise::class], version = 1)
@TypeConverters(StatusConverter::class, ExercisesListConverter::class, WorkoutConverter::class,
    LocalDateConverter::class)
abstract class GoGymRoomDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun dayDao(): DayDao

    companion object {
        private var instance: GoGymRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): GoGymRoomDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoGymRoomDatabase::class.java,
                    "gogym_database"
                ).allowMainThreadQueries()
                    .addCallback(roomDatabaseCallback(context))
                    .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private fun populateDatabase(context: Context, instance: GoGymRoomDatabase) {
            val regularPushUps = Exercise(
                name = "Regular Push Ups",
                numOfSets = 2,
                duration = 6.minutes,
                repsPerSet = 8
            )

            val squats = Exercise(
                name = "Squats",
                numOfSets = 1,
                duration = 1.minutes,
                repsPerSet = 8
            )

            val exerciseDao = instance.exerciseDao()
            exerciseDao.insertMultipleExercises(listOf(regularPushUps, squats))

            val exercises = listOf(regularPushUps, squats, regularPushUps, squats, regularPushUps)

            val workout = Workout(
                name = "Push Ups",
                exercises = exercises
            )

            val workoutDao = instance.workoutDao()
            workoutDao.insertSingleWorkout(workout)

            val day = Day(
                workoutSession = workout,
                workoutStatus = WorkoutStatus.TODAY
            )

            val dayDao = instance.dayDao()
            dayDao.insertSingleDay(day)
        }
    }
}