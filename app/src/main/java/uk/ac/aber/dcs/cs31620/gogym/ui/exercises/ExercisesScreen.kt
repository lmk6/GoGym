package uk.ac.aber.dcs.cs31620.gogym.ui.exercises

import android.app.Instrumentation.ActivityResult
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.MAX_NAME_LENGTH
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.defaultRoundedCornerShape
import uk.ac.aber.dcs.cs31620.gogym.model.day.DataViewModel
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.pathToDefaultIcon
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomAlertDialog
import uk.ac.aber.dcs.cs31620.gogym.ui.components.CustomCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.ExpandableCard
import uk.ac.aber.dcs.cs31620.gogym.ui.components.SnackBar
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyExercise
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.time.Duration

@Composable
fun ExercisesScreen(
    navController: NavHostController,
    dataViewModel: DataViewModel,
    workoutIDText: String? = null
) {
    val coroutineScope = rememberCoroutineScope()

    val workoutID = workoutIDText?.toLongOrNull()

    val exercises by dataViewModel.exercises.observeAsState(listOf())
    val workout: Workout? = workoutID?.let { dataViewModel.getNonLiveWorkoutByID(workoutID) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var exerciseToChange by remember { mutableStateOf<Exercise?>(null) }
    var showSuccessSnackBar by remember { mutableStateOf(false) }

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
                        "${exercise.numOfSets} " + if (exercise.dropSetsFeature) " Drop" else "" +
                                "Set" + if (exercise.numOfSets != 1) "s" else ""

                    val duration =
                        "~${exercise.getFormattedDuration()}"
                    val cannotDeleteText = stringResource(id = R.string.cannotDeleteExercise)

                    if (workout == null)
                        ExpandableCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = exercise.imagePath,
                            topText = exercise.name,
                            bottomText = setsText,
                            extraText = duration,
                            topButtonText = stringResource(id = R.string.editExercise),
                            topButtonImageVector = Icons.Filled.EditNote,
                            bottomButtonText = stringResource(id = R.string.DeleteExercise),
                            bottomButtonImageVector = Icons.Filled.Delete,
                            onClickBottomButton = {
                                if (exercises.size > 1) {
                                    exerciseToChange = exercise
                                    showConfirmationDialog = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        cannotDeleteText,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        )
                    else
                        CustomCard(
                            modifier = Modifier.padding(top = 10.dp),
                            imagePath = exercise.imagePath,
                            topText = exercise.name,
                            bottomText = setsText,
                            extraText = duration,
                            clickAction = {
                                val updatedWorkout = workout.copy(
                                    exercisesIDs = workout.exercisesIDs.plus(exercise.id)
                                )
                                updatedWorkout.totalDuration =
                                    updatedWorkout.totalDuration.plus(exercise.duration)

                                dataViewModel.updateWorkout(updatedWorkout)

                                navController.navigate("${Screen.WorkoutView.route}/${updatedWorkout.id}") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                }
            }
        }
        if (showConfirmationDialog) {
            CustomAlertDialog(
                titleText = stringResource(id = R.string.areYouSure),
                descriptionText = stringResource(id = R.string.confirmPermDeletion),
                onDismiss = {
                    showConfirmationDialog = false
                },
                onConfirm = {
                    dataViewModel.deleteExercise(exerciseToChange!!)
                    showConfirmationDialog = false
                    showSuccessSnackBar = true
                }
            )
        }
        if (showSuccessSnackBar) {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.exerciseDeleted),
                Toast.LENGTH_LONG
            ).show()
            showSuccessSnackBar = false
        }
    }
}

@Suppress("Since15")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ExerciseDialog(
    exercise: Exercise? = null,
    onDismiss: () -> Unit,
    onConfirmation: (Exercise) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            shape = defaultRoundedCornerShape,
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(
                        id = exercise?.let { R.string.editExercise } ?: R.string.createExercise
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                var nameText by remember {
                    mutableStateOf(
                        exercise?.name ?: ""
                    )
                }
                var numOfSetsText by remember {
                    mutableStateOf(
                        "${exercise?.numOfSets ?: 1}"
                    )
                }
                var numOfRepsText by remember {
                    mutableStateOf(
                        "${exercise?.repsPerSet ?: 1}"
                    )
                }

                var imageUri: Uri? by remember {
                    mutableStateOf(
                        Uri.parse(exercise?.imagePath ?: pathToDefaultIcon)
                    )
                }
                val imagePicker = rememberLauncherForActivityResult(
                    ActivityResultContracts.PickVisualMedia()
                ) {
                    if (it != null) imageUri = it
                }

                val duration = exercise?.duration ?: Duration.ZERO

                var hoursText by remember {
                    mutableStateOf(
                        "%02d".format(duration.toHours())
                    )
                }
                var minutesText by remember {
                    mutableStateOf(
                        "%02d".format(duration.toMinutesPart())
                    )
                }
                var secondsText by remember {
                    mutableStateOf(
                        "%02d".format(duration.toSecondsPart())
                    )
                }
                var weightEnabled by remember { mutableStateOf(exercise?.weightEnabled ?: false) }
                var weightText by remember {
                    mutableStateOf(
                        "%02.1f".format(exercise?.weightOne ?: 0)
                    )
                }
                var dropSetsEnabled by remember { mutableStateOf(exercise?.weightEnabled ?: false) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { if (it.length <= MAX_NAME_LENGTH) nameText = it },
                        maxLines = 1,
                        label = { Text(stringResource(R.string.title)) },
                        supportingText = {
                            Text(
                                text = "${nameText.length} / $MAX_NAME_LENGTH",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        },
                        modifier = Modifier.width(230.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .aspectRatio(1f)
                            .clip(shape = defaultRoundedCornerShape)
                            .clickable {
                                imagePicker.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts
                                            .PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                    ) {
                        GlideImage(
                            model = imageUri,
                            contentDescription = stringResource(R.string.pickedImage),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(
                                color = Color.Gray,
                                blendMode = BlendMode.Darken
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                        )

                        Text(
                            text = stringResource(R.string.changeImage),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }

                    OutlinedTextField(
                        value = numOfSetsText,
                        onValueChange = {
                            if (it.length < 3 && it.isNotBlank())
                                numOfSetsText = when (it.toIntOrNull()) {
                                    in 0..99 -> it
                                    else -> numOfSetsText
                                }
                        },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            capitalization = KeyboardCapitalization.None
                        ),
                        label = { Text(stringResource(R.string.sets)) },
                        supportingText = {
                            Text(
                                text = "max 99",
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 10.sp
                            )
                        },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier.width(75.dp),
                    )
                    OutlinedTextField(
                        value = numOfRepsText,
                        onValueChange = {
                            if (it.length < 3 && it.isNotBlank())
                                numOfRepsText = when (it.toIntOrNull()) {
                                    in 0..99 -> it
                                    else -> numOfRepsText
                                }
                        },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            capitalization = KeyboardCapitalization.None
                        ),
                        label = { Text(stringResource(R.string.reps)) },
                        supportingText = {
                            Text(
                                text = "max 99",
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 10.sp
                            )
                        },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier.width(75.dp),
                    )
                }

                Row {
                    Text(stringResource(id = R.string.completionTime))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = hoursText,
                        onValueChange = {
                            if (it.length < 3 && it.isNotBlank())
                                hoursText = when (it.toIntOrNull()) {
                                    in 0..59 -> it
                                    else -> hoursText
                                }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            capitalization = KeyboardCapitalization.None
                        ),
                        maxLines = 1,
                        label = { Text(stringResource(R.string.hours)) },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier.width(70.dp),
                    )
                    OutlinedTextField(
                        value = minutesText,
                        onValueChange = {
                            if (it.length < 3 && it.isNotBlank())
                                minutesText = when (it.toIntOrNull()) {
                                    in 0..59 -> it
                                    else -> minutesText
                                }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            capitalization = KeyboardCapitalization.None
                        ),
                        maxLines = 1,
                        label = { Text(stringResource(R.string.minutes)) },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier.width(70.dp),
                    )
                    OutlinedTextField(
                        value = secondsText,
                        onValueChange = {
                            if (it.length < 3 && it.isNotBlank())
                                secondsText = when (it.toIntOrNull()) {
                                    in 0..59 -> it
                                    else -> secondsText
                                }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            capitalization = KeyboardCapitalization.None
                        ),
                        maxLines = 1,
                        label = { Text(stringResource(R.string.seconds)) },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier.width(70.dp),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Checkbox(
                        checked = weightEnabled,
                        onCheckedChange = {
                            weightEnabled = !weightEnabled
                            if (!weightEnabled) dropSetsEnabled = false
                        },
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.weight),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    if (weightEnabled)
                        OutlinedTextField(
                            value = weightText,
                            onValueChange = {
                                if (it.isNotBlank() && it.length < 6)
                                    weightText = when (it.toFloatOrNull()) {
                                        null -> weightText
                                        else -> it
                                    }
                            },
                            maxLines = 1,
                            label = { Text(stringResource(R.string.kilograms)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                capitalization = KeyboardCapitalization.None
                            ),
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            modifier = Modifier.width(100.dp)
                        )

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    if (weightEnabled) {
                        Checkbox(
                            checked = dropSetsEnabled,
                            onCheckedChange = {
                                dropSetsEnabled = !dropSetsEnabled
                            },
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.dropSet),
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun ExerciseDialogPreview() {
    GoGymTheme {
        ExerciseDialog(
            exercise = dummyExercise,
            onDismiss = { /*TODO*/ },
            onConfirmation = {
            }
        )
    }
}
