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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.day.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomAlertDialog
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.SnackBar
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen

@Composable
fun WorkoutsScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val workouts by dataViewModel.workouts.observeAsState(listOf())
    // If I remove this line, DataViewModel will not load days for a reason I cannot explain
    val days by dataViewModel.days.observeAsState(listOf())
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var workoutToDelete by remember { mutableStateOf<Workout?>(null) }
    var showDeletionProgressSnackBar by remember { mutableStateOf(false) }
    var successfulDeletion = false

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = stringResource(id = R.string.ListOfSessions),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ }
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
                items(workouts) {
                    val workout = it

                    val setsText =
                        "${workout.exercisesIDs.size} " +
                                "Set${if (workout.exercisesIDs.size != 1) "s" else ""}"

                    val duration =
                        "~${workout.getFormattedDuration()}"

                    val cannotDeleteText = stringResource(id = R.string.cannotDeleteWorkout)

                    ExpandableCard(
                        modifier = Modifier.padding(top = 10.dp),
                        imagePath = workout.imagePath,
                        topText = workout.name,
                        bottomText = setsText,
                        extraText = duration,
                        topButtonText = stringResource(id = R.string.editWorkout),
                        topButtonImageVector = Icons.Filled.EditNote,
                        bottomButtonText = stringResource(id = R.string.deleteWorkout),
                        bottomButtonImageVector = Icons.Filled.Delete,
                        onClickTopButton = {
                            navController.navigate("${Screen.WorkoutView.route}/${it.id}") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
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
                }
            }
        }
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
            val ctx = LocalContext.current
            if (successfulDeletion)
                Toast.makeText(
                    ctx,
                    stringResource(id = R.string.workoutDeleted),
                    Toast.LENGTH_LONG
                ).show()
            else
                Toast.makeText(
                    ctx,
                    stringResource(id = R.string.onlyWorkoutAssigned),
                    Toast.LENGTH_LONG
                ).show()
            showDeletionProgressSnackBar = false
        }
    }
}
