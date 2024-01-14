package uk.ac.aber.dcs.cs31620.gogym.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.model.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TodayWorkoutCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

@Composable
fun HomeScreenTopLevel(
    navController: NavHostController,
    dataViewModel: DataViewModel
) {
    val workoutsList by dataViewModel.workouts.observeAsState(listOf())
    val todayWorkout by dataViewModel.getTodayWorkout().observeAsState()

    HomeScreen(
        navController = navController,
        workoutsList = workoutsList,
        todayWorkout = todayWorkout
    )
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    workoutsList: List<Workout> = listOf(),
    todayWorkout: Workout?
) {
    val coroutineScope = rememberCoroutineScope()
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val state = rememberLazyGridState()

            TodayWorkoutCard(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 10.dp
                    ),
                workout = todayWorkout,
                clickAction = { id ->
                    navController.navigate("${Screen.SessionRun.route}/${id}") {
                        launchSingleTop = true
                    }
                }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                state = state,
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                items(workoutsList) {

                    val topText = it.name
                    val numOfExercises = it.exercisesIDs.size
                    val bottomText =
                        "$numOfExercises Exercise${if (numOfExercises != 1) "s" else ""}"
                    val extraText = "~${it.getFormattedDuration()}"

                    CustomCard(
                        imagePath = it.imagePath,
                        topText = topText,
                        bottomText = bottomText,
                        extraText = extraText,
                        clickAction = {
                            navController.navigate("${Screen.WorkoutView.route}/${it.id}") {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    GoGymTheme(dynamicColor = false) {
        HomeScreen(
            navController = navController,
            todayWorkout = null
        )
    }
}
