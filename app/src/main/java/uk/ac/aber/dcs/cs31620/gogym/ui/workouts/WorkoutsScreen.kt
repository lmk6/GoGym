package uk.ac.aber.dcs.cs31620.gogym.ui.workouts

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
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.pathToRestDayImage
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomAlertDialog
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.SnackBar
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen

/**
 * Displays workouts for view and selection
 * @param dayIDText if provides a valid id, it will display workouts for selections
 * otherwise, it will display workouts for view and delete purposes
 */
@Composable
fun WorkoutsScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel,
    dayIDText: String? = null
) {
    val coroutineScope = rememberCoroutineScope()

    val dayID = dayIDText?.toLongOrNull()

    val workouts by dataViewModel.workouts.observeAsState(listOf())
    val day: Day? = dayID?.let { dataViewModel.getNonLiveDayByID(dayID) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showCreateNewDialog by remember { mutableStateOf(false) }
    var workoutToDelete by remember { mutableStateOf<Workout?>(null) }
    var showDeletionProgressSnackBar by remember { mutableStateOf(false) }

    var successfulDeletion = false

    val context = LocalContext.current

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = stringResource(id = R.string.ListOfSessions),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateNewDialog = true }
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

            day?.let {
                EmptySessionCard {
                    val updatedDay = day.copy(workoutID = null)
                    dataViewModel.updateDay(updatedDay)
                    navController.navigate(Screen.WeekPlanner.route) {
                        popUpTo(Screen.WeekPlanner.route)
                        launchSingleTop = true
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                state = state,
                contentPadding = PaddingValues(10.dp)
            ) {
                items(workouts) {
                    val workout = it

                    val exercisesText =
                        "${workout.exercisesIDs.size} " +
                                "Exercise${if (workout.exercisesIDs.size != 1) "s" else ""}"

                    val duration =
                        "~${workout.getFormattedDuration()}"

                    val cannotDeleteText = stringResource(id = R.string.cannotDeleteWorkout)

                    if (day == null)
                        ExpandableCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = workout.imagePath,
                            topText = workout.name,
                            bottomText = exercisesText,
                            extraText = duration,
                            topButtonText = stringResource(id = R.string.editWorkout),
                            topButtonImageVector = Icons.Filled.EditNote,
                            bottomButtonText = stringResource(id = R.string.deleteWorkout),
                            bottomButtonImageVector = Icons.Filled.Delete,
                            onClickTopButton = {
                                navController.navigate("${Screen.WorkoutView.route}/${it.id}") {
                                    popUpTo("${Screen.WorkoutView.route}/${it.id}")
                                    launchSingleTop = true
                                }
                            },
                            onClickBottomButton = {
                                if (workouts.size > 1) {
                                    workoutToDelete = workout
                                    showConfirmationDialog = true
                                } else
                                    Toast.makeText(
                                        context,
                                        cannotDeleteText,
                                        Toast.LENGTH_LONG
                                    ).show()
                            }
                        )
                    else
                        CustomCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = workout.imagePath,
                            topText = workout.name,
                            bottomText = exercisesText,
                            extraText = duration,
                            clickAction = {
                                val updatedDay = day.copy(workoutID = workout.id)
                                dataViewModel.updateDay(updatedDay)
                                navController.navigate(Screen.WeekPlanner.route) {
                                    popUpTo(Screen.WeekPlanner.route)
                                    launchSingleTop = true
                                }
                            }
                        )
                }
            }
        }
        /**
         * Dialogs and SnackBars section
         */

        if (showConfirmationDialog) {
            CustomAlertDialog(
                titleText = stringResource(id = R.string.areYouSure),
                descriptionText = stringResource(id = R.string.confirmPermDeletion),
                onDismiss = { showConfirmationDialog = false },
                onConfirm = {
                    successfulDeletion = dataViewModel.deleteWorkout(workoutToDelete!!)
                    showConfirmationDialog = false
                    showDeletionProgressSnackBar = true
                }
            )
        }

        if (showDeletionProgressSnackBar) {
            if (successfulDeletion)
                Toast.makeText(
                    context,
                    stringResource(id = R.string.workoutDeleted),
                    Toast.LENGTH_LONG
                ).show()
            else
                Toast.makeText(
                    context,
                    stringResource(id = R.string.onlyWorkoutAssigned),
                    Toast.LENGTH_LONG
                ).show()
            showDeletionProgressSnackBar = false
        }

        if (showCreateNewDialog) {
            WorkoutDialog(
                onDismiss = {
                    showCreateNewDialog = false
                },
                onConfirm = { newWorkout ->
                    val id = dataViewModel.insertWorkout(newWorkout)
                    navController.navigate("${Screen.Exercises.route}/${id}") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

/**
 * Selecting it will schedule a rest day
 */
@Composable
private fun EmptySessionCard(clickAction: () -> Unit) {
    CustomCard(
        modifier = Modifier.padding(
            top = 10.dp,
            start = 10.dp,
            end = 10.dp
        ),
        imagePath = pathToRestDayImage,
        topText = stringResource(R.string.restDay),
        bottomText = stringResource(R.string.noSession),
        clickAction = { clickAction() })
}
