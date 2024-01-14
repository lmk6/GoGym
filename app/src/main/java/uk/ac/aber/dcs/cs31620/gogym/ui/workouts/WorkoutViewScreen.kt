package uk.ac.aber.dcs.cs31620.gogym.ui.workouts

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.SnackBar
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen

@Composable
fun WorkoutViewScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel,
    workoutIDText: String? = null
) {
    if (workoutIDText == null) navController.navigate(Screen.Home.route)
    val workoutID = workoutIDText?.toLongOrNull()!! // I am confident it will never be null
    val coroutineScope = rememberCoroutineScope()
    val workoutLiveData by dataViewModel.getWorkoutByID(workoutID).observeAsState()
    workoutLiveData?.let { workout ->
        val exercises by dataViewModel.getWorkoutExercises(workout).observeAsState(listOf())
        val selectedExercises = workout.exercisesIDs.mapNotNull { id ->
            exercises.find { exercise ->
                exercise.id == id
            }
        }
        var showChangeDialog by remember { mutableStateOf(false) }

        TopLevelScaffold(
            navController = navController,
            coroutineScope = coroutineScope,
            title = workout.name,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("${Screen.Exercises.route}/${workout.id}") {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.createNew)
                    )
                }
            },
            snackbarHostState = SnackbarHostState(),
            snackbarContent = { data ->
                SnackBar(
                    data = data,
                    modifier = Modifier.padding(bottom = 4.dp),
                    onDismiss = {
                        data.dismiss()
                    }
                )
            },
            topAppBarActionContent = {
                IconButton(onClick = {
                    showChangeDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.EditNote,
                        contentDescription = stringResource(id = R.string.editWorkout),
                        modifier = Modifier.size(128.dp)
                    )
                }
            },
            navigationBarTopButton = {
                StartSessionButton {
                    navController.navigate("${Screen.SessionRun.route}/${workout.id}") {
                        launchSingleTop = true
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val state = rememberLazyGridState()
                val context = LocalContext.current

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    state = state,
                    contentPadding = PaddingValues(10.dp)
                ) {
                    itemsIndexed(selectedExercises) { index, exercise ->

                        val setsText =
                            "${exercise.numOfSets} " +
                                    if (exercise.dropSetsFeatureEnabled) " Drop" else "" +
                                            "Set" + if (exercise.numOfSets != 1) "s" else ""

                        val duration =
                            "~${exercise.getFormattedDuration()}"

                        val deletedText = stringResource(id = R.string.exerciseRemoved)
                        val cannotDeleteText = stringResource(id = R.string.cannotDeleteExercise)

                        ExpandableCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = exercise.imagePath,
                            topText = exercise.name,
                            bottomText = setsText,
                            extraText = duration,
                            topButtonText = stringResource(id = R.string.removeFromList),
                            topButtonImageVector = Icons.Filled.Delete,
                            onClickTopButton = {
                                if (workout.exercisesIDs.size > 1) {
                                    val newExercisesIDs =
                                        workout.exercisesIDs.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    val updatedWorkout =
                                        workout.copy(exercisesIDs = newExercisesIDs)

                                    updatedWorkout.totalDuration =
                                        updatedWorkout.totalDuration.minus(exercise.duration)

                                    dataViewModel.updateWorkout(updatedWorkout)

                                    Toast.makeText(
                                        context,
                                        deletedText,
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        cannotDeleteText,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            detailsList = getExerciseDetails(exercise)
                        )
                    }
                }
            }
            if (showChangeDialog) {
                WorkoutDialog(
                    workout,
                    onDismiss = { showChangeDialog = false },
                    onConfirm = {
                        if (workout.exercisesIDs.isNotEmpty())
                            dataViewModel.updateWorkout(it)
                    }
                )
            }
        }
    }
}

private fun getExerciseDetails(exercise: Exercise): List<String> {
    val weightText = if (exercise.weightEnabled) "Weight: ${exercise.weightOne}"
    else "No Weight"
    val repsText = if (exercise.dropSetsFeatureEnabled) "" else {
        "${exercise.repsPerSet} Reps"
    }
    return listOf(
        "~${exercise.getFormattedDuration()}",
        "${exercise.numOfSets} " + if (exercise.dropSetsFeatureEnabled) "Drop Set" else {
            "Set"
        } + if (exercise.numOfSets > 1) "s" else "",
        weightText,
        repsText
    )
}

@Composable
private fun StartSessionButton(
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
    ) {
        Button(
            onClick = { onClick() },
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp
                    )
                )
        ) {
            Text(
                text = stringResource(id = R.string.start),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
        }
    }
}
