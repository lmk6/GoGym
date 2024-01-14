package uk.ac.aber.dcs.cs31620.gogym

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.model.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.ui.exercises.ExercisesScreen
import uk.ac.aber.dcs.cs31620.gogym.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import uk.ac.aber.dcs.cs31620.gogym.ui.week_planner.WeekPlannerScreen
import uk.ac.aber.dcs.cs31620.gogym.ui.workouts.WorkoutViewScreen
import uk.ac.aber.dcs.cs31620.gogym.ui.workouts.WorkoutsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoGymTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }
}

@Composable
fun BuildNavigationGraph(
    dataViewModel: DataViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val startDestination = remember { Screen.Home.route }

    val context = LocalContext.current as Activity

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) { HomeScreenTopLevel(navController, dataViewModel) }
        composable(Screen.WeekPlanner.route) { WeekPlannerScreen(navController, dataViewModel) }
        composable(Screen.Exercises.route) { ExercisesScreen(navController, dataViewModel)}
        composable("${Screen.Exercises.route}/{workoutID}") { backStackEntry ->
            ExercisesScreen(
                navController,
                dataViewModel,
                backStackEntry.arguments?.getString("workoutID")
            )
        }
        composable("${Screen.WorkoutView.route}/{workoutID}") { backStackEntry ->
            WorkoutViewScreen(
                navController,
                dataViewModel,
                backStackEntry.arguments?.getString("workoutID")
            )
        }
        composable(Screen.Sessions.route) { WorkoutsScreen(navController, dataViewModel) }
        composable("${Screen.Sessions.route}/{dayID}") { backStackEntry ->
            WorkoutsScreen(
                navController,
                dataViewModel,
                backStackEntry.arguments?.getString("dayID")
            )
        }
    }
}
