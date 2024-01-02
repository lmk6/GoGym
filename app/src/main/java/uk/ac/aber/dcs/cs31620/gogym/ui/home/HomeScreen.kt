package uk.ac.aber.dcs.cs31620.gogym.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.model.Day
import uk.ac.aber.dcs.cs31620.gogym.model.Workout
import uk.ac.aber.dcs.cs31620.gogym.model.WorkoutStatus
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.components.WeekDayNavigationBar
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.time.LocalDate

@Composable
fun HomeScreenTopLevel(
    navController: NavHostController
) {
    HomeScreen(navController = navController)
}

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeScreenContent(
                modifier = Modifier.padding(8.dp)
            )
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
    val days = listOf(
        Day(LocalDate.now().minusDays(1), Workout(), WorkoutStatus.COMPLETED),
        Day(LocalDate.now(), Workout(), WorkoutStatus.TODAY),
        Day(LocalDate.now().plusDays(1), Workout(), WorkoutStatus.IN_THE_FUTURE)
    )
    var day by remember { mutableStateOf(days[1]) }

    WeekDayNavigationBar(
        chosenDay = remember { mutableStateOf(day) },
        daysList = days,
        onDaySelected = { nDay -> day = nDay },
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))


}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    GoGymTheme(dynamicColor = false) {
        HomeScreen(navController = navController)
    }
}
