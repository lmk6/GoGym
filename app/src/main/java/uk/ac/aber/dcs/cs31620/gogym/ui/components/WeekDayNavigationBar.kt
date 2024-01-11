package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.model.workout.WorkoutStatus
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.time.LocalDate

@Composable
fun WeekDayNavigationBar(
    modifier: Modifier = Modifier,
    chosenDay: MutableState<Day>,
    daysList: List<Day>,
    onDaySelected: (Day) -> Unit
) {

    val scrollState = rememberLazyListState()

    LazyRow(
        state = scrollState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        content = {
            items(daysList.size) { index ->
                val day = daysList[index]
                val isSelected = chosenDay.value.dayOfWeek == day.dayOfWeek
                val fontSize = if (isSelected) 40.sp else 24.sp

                DaySelectionItem(day, isSelected, fontSize) {
                    onDaySelected(day)
                }
            }
        }
    )
}

@Composable
fun DaySelectionItem(
    day: Day,
    isSelected: Boolean,
    fontSize: TextUnit,
    onItemClick: () -> Unit
) {
    var textCol = MaterialTheme.colorScheme.onTertiaryContainer
//    if (day.workoutStatus == WorkoutStatus.COMPLETED)
//        textCol = Color(89, 210, 123)
//    else if (day.workoutStatus == WorkoutStatus.UNCOMPLETED)
//        textCol = MaterialTheme.colorScheme.error

    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick() }
    ) {
        Text(
            text = day.dayOfWeek.toString(),
            color = textCol,
            fontSize = fontSize,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun WeekDayNavigationPreview() {
//    val days = listOf(
//        Day(LocalDate.now().minusDays(1), Workout(), WorkoutStatus.COMPLETED),
//        Day(LocalDate.now(), Workout(), WorkoutStatus.TODAY),
//        Day(LocalDate.now().plusDays(1), Workout(), WorkoutStatus.IN_THE_FUTURE)
//    )
//    var day by remember { mutableStateOf(days[1]) }
//    GoGymTheme(dynamicColor = false) {
//        WeekDayNavigationBar(
//            chosenDay = remember { mutableStateOf(day) },
//            daysList = days,
//            onDaySelected = { nDay -> day = nDay})
//    }
}
