package uk.ac.aber.dcs.cs31620.gogym.ui.workouts

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.NUM_OF_DROP_MINI_SETS
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyExercise
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyWorkout
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.util.Locale

/**
 * Top Level Of the Screen.
 * @param workout - the session to be run.
 */
@Composable
fun SessionRunTopLevelScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel,
    workout: Workout
) {
    val exercises by dataViewModel.getWorkoutExercises(workout).observeAsState(listOf())
    val selectedExercises = workout.exercisesIDs.mapNotNull { id ->
        exercises.find { exercise ->
            exercise.id == id
        }
    }
    if (selectedExercises.isNotEmpty())
        SessionRunScreen(
            navController,
            selectedExercises,
            workout.name
        )

}

/**
 * Screen responsible for running the workout Session
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SessionRunScreen(
    navController: NavHostController,
    exercises: List<Exercise> = listOf(),
    workoutName: String
) {
    val coroutineScope = rememberCoroutineScope()
    var currentExerciseIndex by remember { mutableIntStateOf(0) }
    var currentExercise by remember { mutableStateOf(exercises.first()) }
    var currentWeight by remember { mutableFloatStateOf(currentExercise.weightOne) }

    var currentSet by remember { mutableIntStateOf(1) }
    var currentMiniSet by remember { mutableIntStateOf(1) }
    var buttonStringResource by remember {
        mutableIntStateOf(
            if (currentExercise.dropSetsFeatureEnabled) R.string.failure
            else R.string.next
        )
    }
    var isBreak by remember { mutableStateOf(false) }

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = workoutName
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Card(
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 20.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
            ) {
                Row {
                    GlideImage(
                        model = Uri.parse(currentExercise.imagePath),
                        contentDescription = stringResource(id = R.string.exerciseImage),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.4f)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentExercise.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                /**
                 * String creation, they get updated each recomposition
                 */
                val durationText =
                    "${stringResource(R.string.approxTime)}: ~${currentExercise.getFormattedDuration()}"

                val weightText =
                    if (currentExercise.weightEnabled)
                        "${stringResource(R.string.weight)}: ${currentWeight}kg"
                    else
                        stringResource(R.string.noWeight)

                val setsText = "Set: $currentSet / ${currentExercise.numOfSets}"

                val repsText = if (!currentExercise.dropSetsFeatureEnabled)
                    "${currentExercise.repsPerSet} Reps per Set"
                else stringResource(id = R.string.asManyReps)

                /**
                 * It would not make much sense to display "Failure: 1/3" from start
                 */
                val whichFailureText = if (currentExercise.dropSetsFeatureEnabled) {
                    "${stringResource(R.string.failure)}: ${currentMiniSet - 1} / 3"
                } else null

                ExerciseSetInfo(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .padding(start = 25.dp, end = 25.dp),
                    duration = durationText,
                    weight = weightText,
                    whichSet = setsText,
                    repsPerSet = repsText,
                    whichFailure = whichFailureText,
                    buttonText = stringResource(buttonStringResource),
                    isBreak = isBreak
                ) {
                    /**
                     * logic behind the session go-through
                     */
                    if (isBreak) {
                        isBreak = false
                        buttonStringResource =
                            if (currentSet == currentExercise.numOfSets) {
                                if (currentMiniSet == NUM_OF_DROP_MINI_SETS) {
                                    if (currentExerciseIndex == exercises.size - 1) R.string.finish
                                    else R.string.nextExercise
                                } else R.string.next
                            }
                            else R.string.failure
                    } else if (currentExercise.dropSetsFeatureEnabled
                        && !(currentMiniSet == NUM_OF_DROP_MINI_SETS
                                && currentSet == currentExercise.numOfSets)
                    ) {
                        if (currentMiniSet == NUM_OF_DROP_MINI_SETS) {
                            currentSet++
                            currentMiniSet = 1
                            buttonStringResource = R.string.longBreak

                        } else if (currentMiniSet < NUM_OF_DROP_MINI_SETS) {
                            currentMiniSet++
                            buttonStringResource = R.string.shortBreak
                        }
                        currentWeight = when (currentMiniSet) {
                            1 -> currentExercise.weightOne
                            2 -> currentExercise.weightTwo
                            3 -> currentExercise.weightThree
                            else -> currentExercise.weightThree
                        }
                        isBreak = true
                    } else if (currentSet == currentExercise.numOfSets) {
                        if (currentExerciseIndex < exercises.size - 1
                        ) {
                            currentSet = 1
                            currentExercise = exercises[++currentExerciseIndex]
                            buttonStringResource = R.string.next
                        } else {
                            navigateBackToHome(navController)
                        }
                    } else {
                        buttonStringResource =
                            if (currentSet == currentExercise.numOfSets - 1) {
                                if (currentExerciseIndex == exercises.size - 1) R.string.finish
                                else R.string.nextExercise
                            } else R.string.next
                        currentSet++
                    }
                }
            }
        }
    }
}

private fun navigateBackToHome(
    navController: NavHostController
) {
    navController.navigate(Screen.Home.route) {
        popUpTo(navController.graph.findStartDestination().id)
        launchSingleTop = true
    }
}

/**
 * Displays all the Set information
 * Also Holds the button for the layout reasons
 */
@Composable
private fun ExerciseSetInfo(
    modifier: Modifier = Modifier,
    duration: String,
    weight: String,
    whichSet: String,
    buttonText: String,
    repsPerSet: String,
    whichFailure: String? = null,
    isBreak: Boolean,
    onButtonClick: () -> Unit,
) {
    val fontSize = 20.sp
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = duration,
                fontSize = fontSize
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = weight,
                fontSize = fontSize
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = whichSet,
                fontSize = fontSize * 1.5
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = repsPerSet,
                fontSize = fontSize
            )
            Spacer(Modifier.height(5.dp))
            whichFailure?.let {
                Text(
                    text = whichFailure,
                    fontSize = fontSize
                )
            }

        }

        Button(
            shape = RectangleShape,
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (isBreak) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.primary,
                contentColor =
                if (isBreak) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
        ) {
            Text(
                text = buttonText.uppercase(Locale.getDefault()),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun SessionRunScreenPreview() {
    val navController = rememberNavController()
    GoGymTheme(darkTheme = true) {
        SessionRunScreen(
            exercises = listOf(dummyExercise),
            navController = navController,
            workoutName = dummyWorkout.name
        )
    }
}
