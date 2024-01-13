package uk.ac.aber.dcs.cs31620.gogym.ui.week_planner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.day.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import java.util.Locale

@Composable
fun WeekPlannerScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val week by dataViewModel.days.observeAsState(listOf())

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = stringResource(id = R.string.weekPlanner)
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
                    val workout = day.workoutID?.let { dataViewModel.getDaysWorkout(day) }
                    val imagePath =
                        workout?.imagePath
                            ?: "file:///android_asset/images/eirik_uhlen_rest_day.jpg"
                    val workoutName = workout?.name ?: stringResource(id = R.string.noSession)
                    val dayName = day.dayOfWeek.toString().lowercase()
                        .replaceFirstChar { itDay ->
                            if (itDay.isLowerCase()) itDay.titlecase(Locale.getDefault())
                            else itDay.toString()
                        }

                    ExpandableCard(
                        modifier = Modifier.padding(top = 10.dp),
                        imagePath = imagePath,
                        topText = dayName,
                        bottomText = workoutName,
                        topButtonImageVector = Icons.Rounded.ChangeCircle,
                        topButtonText = stringResource(id = R.string.editWorkout),
                        bottomButtonImageVector = Icons.Rounded.RemoveRedEye,
                        bottomButtonText = stringResource(id = R.string.viewWorkout)
                    )
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
