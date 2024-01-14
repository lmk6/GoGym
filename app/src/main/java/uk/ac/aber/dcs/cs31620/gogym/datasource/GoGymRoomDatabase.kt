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
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.DurationConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.ExercisesIDsListConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.LocalDateConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.StatusConverter
import uk.ac.aber.dcs.cs31620.gogym.datasource.util.WorkoutConverter
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayDao
import uk.ac.aber.dcs.cs31620.gogym.model.day.DayOfWeek
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.ExerciseDao
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutDao
import uk.ac.aber.dcs.cs31620.gogym.pathToPushUpsImage
import uk.ac.aber.dcs.cs31620.gogym.pathToSquatImage
import java.time.Duration

@Database(entities = [Day::class, Workout::class, Exercise::class], version = 1)
@TypeConverters(
    StatusConverter::class, ExercisesIDsListConverter::class, WorkoutConverter::class,
    LocalDateConverter::class, DurationConverter::class
)
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
                duration = Duration.ofMinutes(6),
                repsPerSet = 8,
                imagePath = pathToPushUpsImage
            )

            val squats = Exercise(
                name = "Squats",
                numOfSets = 1,
                duration = Duration.ofMinutes(1).plusSeconds(30),
                repsPerSet = 8,
                imagePath = pathToSquatImage
            )

            val exerciseDao = instance.exerciseDao()
            val ex1ID = exerciseDao.insertSingleExercise(regularPushUps)
            val ex2ID = exerciseDao.insertSingleExercise(squats)

            val exercises = listOf(ex1ID, ex2ID, ex1ID, ex2ID, ex1ID)

            val workoutDao = instance.workoutDao()

            val workout = Workout(
                name = "Push Ups",
                imagePath = pathToPushUpsImage,
                exercisesIDs = exercises,
                totalDuration = regularPushUps.duration
                    .plus(regularPushUps.duration)
                    .plus(regularPushUps.duration)
                    .plus(squats.duration).plus(squats.duration)
            )

            val workout1 = Workout(
                name = "PushUps",
                imagePath = pathToSquatImage,
                exercisesIDs = listOf(ex1ID, ex2ID),
                totalDuration = regularPushUps.duration.plus(squats.duration)
            )

            val workoutID = workoutDao.insertSingleWorkout(workout)
            val workoutID2 = workoutDao.insertSingleWorkout(workout1)

            val days = listOf(
                Day(
                    workoutID = workoutID,
                    dayOfWeek = DayOfWeek.MONDAY
                ),
                Day(
                    dayOfWeek = DayOfWeek.TUESDAY
                ),
                Day(
                    dayOfWeek = DayOfWeek.WEDNESDAY
                ),
                Day(
                    workoutID = workoutID,
                    dayOfWeek = DayOfWeek.THURSDAY
                ),
                Day(
                    dayOfWeek = DayOfWeek.FRIDAY
                ),
                Day(
                    workoutID = workoutID2,
                    dayOfWeek = DayOfWeek.SATURDAY
                ),
                Day(
                    dayOfWeek = DayOfWeek.SUNDAY
                ),
            )
            val dayDao = instance.dayDao()
            dayDao.insertMultipleDays(days)
        }
    }
}