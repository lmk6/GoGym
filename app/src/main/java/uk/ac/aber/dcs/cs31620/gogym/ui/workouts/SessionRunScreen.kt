package uk.ac.aber.dcs.cs31620.gogym.ui.workouts

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.defaultRoundedCornerShape
import uk.ac.aber.dcs.cs31620.gogym.model.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyExercise
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyWorkout
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

@Composable
fun SessionRunTopLevelScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel,
    workout: Workout
) {
    val exercises by dataViewModel.getWorkoutExercises(workout).observeAsState(listOf())

    SessionRunScreen(
        navController,
        exercises,
        workout.name
    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SessionRunScreen(
    navController: NavHostController,
    exercises: List<Exercise> = listOf(),
    workoutName: String
) {
    val coroutineScope = rememberCoroutineScope()
    val currentExerciseIndex by remember { mutableIntStateOf(0) }
    val currentExercise by remember { mutableStateOf(exercises.first()) }

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        title = workoutName
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Card(
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 20.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
            ) {
                Row {
                    GlideImage(
                        model = Uri.parse(currentExercise.imagePath),
                        contentDescription = stringResource(id = R.string.exerciseImage),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.3f)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentExercise.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseInfo(
    modifier: Modifier = Modifier,
    duration: String,
    weight: String,
    whichSet: String,
    repsPerSet: String?,
    whichFailure: String?,
    onButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
    ) {

    }
}

@Preview
@Composable
fun SessionRunScreenPreview() {
    val navController = rememberNavController()
    GoGymTheme(darkTheme = true) {
        SessionRunScreen(
            exercises = listOf(dummyExercise),
            navController = navController,
            workoutName = dummyWorkout.name
        )
    }
}
