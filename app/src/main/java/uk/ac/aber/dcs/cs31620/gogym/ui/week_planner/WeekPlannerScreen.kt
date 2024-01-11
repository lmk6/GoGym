package uk.ac.aber.dcs.cs31620.gogym.ui.week_planner

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.model.day.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableDayCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

@Composable
fun WeekPlanner(
    navController: NavHostController,
    dataViewModel: DataViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val week by dataViewModel.days.observeAsState(listOf())

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

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                state = state,
                contentPadding = PaddingValues(10.dp)
            ) {
                items(week) {
                    val day = it
                    ExpandableDayCard(
                        modifier = Modifier.padding(top = 10.dp),
                        day = day,
                        workout = day.workoutID?.let {dataViewModel.getDaysWorkout(day)})
                }
            }
        }
    }

}

//@Composable
//@Preview
//fun WeekPlannerPreview() {
//    val navController = rememberNavController()
//    GoGymTheme {
//        WeekPlanner(navController = navController, dataViewModel = )
//    }
//}
