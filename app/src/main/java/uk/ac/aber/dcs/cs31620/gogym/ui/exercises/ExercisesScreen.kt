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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.day.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.SnackBar
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold

@Composable
fun ExercisesScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val exercises by dataViewModel.exercises.observeAsState(listOf())

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = stringResource(id = R.string.ListOfExercises),
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
                items(exercises) {
                    val exercise = it

                    val setsText =
                        "${exercise.numOfSets} Set${if (exercise.numOfSets != 1) "s" else ""}"

                    val duration =
                        "~${exercise.getFormattedDuration()}"

                    val deletedText = stringResource(id = R.string.exerciseDeleted)
                    val cannotDeleteText = stringResource(id = R.string.cannotDelete)

                    ExpandableCard(
                        modifier = Modifier.padding(top = 10.dp),
                        imagePath = exercise.imagePath,
                        topText = exercise.name,
                        bottomText = setsText,
                        extraText = duration,
                        topButtonText = stringResource(id = R.string.editExercise),
                        topButtonImageVector = Icons.Filled.EditNote,
                        bottomButtonText = stringResource(id = R.string.Delete),
                        bottomButtonImageVector = Icons.Filled.Delete,
                        onClickBottomButton = {
                            if (exercises.size > 1) {
                                dataViewModel.deleteExercise(exercise)
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
                        }
                    )
                }
            }
        }
    }
}
