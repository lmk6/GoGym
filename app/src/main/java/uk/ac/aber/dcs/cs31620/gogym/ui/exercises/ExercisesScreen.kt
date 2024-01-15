package uk.ac.aber.dcs.cs31620.gogym.ui.exercises

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomAlertDialog
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.SnackBar
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen

/**
 * Has two modes, if workout ID is valid, the selection of an exercises for the workout.
 * If not, Lists Exercises.
 * Allows to edit, view and delete each one of them.
 * FAB allows the creation of a new Exercise.
 * @param workoutIDText if null, enables the creation, otherwise, the edition of a workout.
 */
@Composable
fun ExercisesScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel,
    workoutIDText: String? = null
) {
    val coroutineScope = rememberCoroutineScope()

    val workoutID = workoutIDText?.toLongOrNull()

    val exercises by dataViewModel.exercises.observeAsState(listOf())
    val workout: Workout? = workoutID?.let { dataViewModel.getNonLiveWorkoutByID(workoutID) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showChangeDialog by remember { mutableStateOf(false) }

    var exerciseToChange by remember { mutableStateOf<Exercise?>(null) }
    var showSuccessSnackBar by remember { mutableStateOf(false) }


    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = stringResource(
            workout?.let { R.string.selectExercise } ?: R.string.ListOfExercises
        ),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    exerciseToChange = null
                    showChangeDialog = true
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
                items(exercises) {
                    val exercise = it

                    val setsText =
                        "${exercise.numOfSets} " + if (exercise.dropSetsFeatureEnabled) "Drop" else "" +
                                "Set" + if (exercise.numOfSets != 1) "s" else ""

                    val duration =
                        "~${exercise.getFormattedDuration()}"
                    val cannotDeleteText = stringResource(id = R.string.cannotDeleteExercise)

                    if (workout == null)
                        ExpandableCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = exercise.imagePath,
                            topText = exercise.name,
                            bottomText = setsText,
                            extraText = duration,
                            topButtonText = stringResource(id = R.string.editExercise),
                            topButtonImageVector = Icons.Filled.EditNote,
                            onClickTopButton = {
                                exerciseToChange = exercise
                                showChangeDialog = true
                            },
                            bottomButtonText = stringResource(id = R.string.DeleteExercise),
                            bottomButtonImageVector = Icons.Filled.Delete,
                            onClickBottomButton = {
                                if (exercises.size > 1) {
                                    exerciseToChange = exercise
                                    showConfirmationDialog = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        cannotDeleteText,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        )
                    else
                        CustomCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = exercise.imagePath,
                            topText = exercise.name,
                            bottomText = setsText,
                            extraText = duration,
                            clickAction = {
                                val updatedWorkout = workout.copy(
                                    exercisesIDs = workout.exercisesIDs.plus(exercise.id)
                                )
                                updatedWorkout.totalDuration =
                                    updatedWorkout.totalDuration.plus(exercise.duration)

                                dataViewModel.updateWorkout(updatedWorkout)

                                navController.navigate("${Screen.WorkoutView.route}/${updatedWorkout.id}") {
                                    popUpTo("${Screen.WorkoutView.route}/${updatedWorkout.id}")
                                    launchSingleTop = true
                                }
                            }
                        )
                }
            }
        }
        if (showConfirmationDialog) {
            CustomAlertDialog(
                titleText = stringResource(id = R.string.areYouSure),
                descriptionText = stringResource(id = R.string.confirmPermDeletion),
                onDismiss = {
                    showConfirmationDialog = false
                },
                onConfirm = {
                    dataViewModel.deleteExercise(exerciseToChange!!)
                    showConfirmationDialog = false
                    showSuccessSnackBar = true
                    exerciseToChange = null
                }
            )
        }
        if (showSuccessSnackBar) {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.exerciseDeleted),
                Toast.LENGTH_LONG
            ).show()
            showSuccessSnackBar = false
        }
        if (showChangeDialog) {
            ExerciseDialog(
                exercise = exerciseToChange,
                onDismiss = {
                    showChangeDialog = false
                    exerciseToChange = null
                },
                onConfirm = { updatedExercise ->
                    exerciseToChange?.let { dataViewModel.updateExercise(updatedExercise) }
                        ?: dataViewModel.insertExercise(updatedExercise)
                }
            )
        }
    }

    // Makes sure that the newly created Workout has at least one exercise
    // If not, it gets automatically deleted
    DisposableEffect(Unit) {
        onDispose {
            workout?.let {
                val onExitWorkout = dataViewModel.getNonLiveWorkoutByID(workout.id)
                onExitWorkout?.let {
                    if (onExitWorkout.exercisesIDs.isEmpty())
                        dataViewModel.deleteWorkout(onExitWorkout)
                }
            }
        }
    }
}

