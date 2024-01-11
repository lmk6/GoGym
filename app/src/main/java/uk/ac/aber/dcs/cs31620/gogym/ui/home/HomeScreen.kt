package uk.ac.aber.dcs.cs31620.gogym.ui.home

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.day.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TodayWorkoutCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.components.WorkoutCard
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
            val context = LocalContext.current

            TodayWorkoutCard(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 10.dp
                    ),
                workout = todayWorkout
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                state = state,
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                items(workoutsList) {
                    WorkoutCard(
                        workout = it,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier
) {
    Text(text = "Hello", modifier = modifier)
}

@Composable
fun DayNavigation() {
//    val days = listOf(
//        Day(LocalDate.now().minusDays(1), Workout(), WorkoutStatus.COMPLETED),
//        Day(LocalDate.now(), Workout(), WorkoutStatus.TODAY),
//        Day(LocalDate.now().plusDays(1), Workout(), WorkoutStatus.IN_THE_FUTURE)
//    )
//    var day by remember { mutableStateOf(days[1]) }
//
//    WeekDayNavigationBar(
//        chosenDay = remember { mutableStateOf(day) },
//        daysList = days,
//        onDaySelected = { nDay -> day = nDay },
//        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
//

}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    GoGymTheme(dynamicColor = false) {
        HomeScreen(navController = navController, todayWorkout = null)
    }
}
