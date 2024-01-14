package uk.ac.aber.dcs.cs31620.gogym.ui.exercises

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.MAX_NAME_LENGTH
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.defaultRoundedCornerShape
import uk.ac.aber.dcs.cs31620.gogym.model.exercise.Exercise
import uk.ac.aber.dcs.cs31620.gogym.pathToDefaultIcon
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyExercise
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.time.Duration

@Suppress("Since15")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ExerciseDialog(
    exercise: Exercise? = null,
    onDismiss: () -> Unit,
    onConfirm: (Exercise) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
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
                        exercise?.name ?: "New Exercise"
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
                        "%02.1f".format(exercise?.weightOne ?: 0f)
                    )
                }
                var dropSetsEnabled by remember { mutableStateOf(exercise?.weightEnabled ?: false) }
                var secondWeightText by remember {
                    mutableStateOf(
                        "%02.1f".format(exercise?.weightOne ?: 0f)
                    )
                }
                var thirdWeightText by remember {
                    mutableStateOf(
                        "%02.1f".format(exercise?.weightOne ?: 0f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = {
                            if (it.isNotBlank() && it.length <= MAX_NAME_LENGTH) nameText = it
                        },
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
                        enabled = !dropSetsEnabled,
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
                        enabled = !dropSetsEnabled,
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
                                if (dropSetsEnabled) numOfSetsText = "3"
                            },
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.dropSet),
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    if (dropSetsEnabled) {
                        OutlinedTextField(
                            value = secondWeightText,
                            onValueChange = {
                                if (it.isNotBlank() && it.length < 6)
                                    secondWeightText = when (it.toFloatOrNull()) {
                                        null -> secondWeightText
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
                        Text(
                            text = stringResource(id = R.string.afterFirstDrop),
                            modifier = Modifier.padding(start = 10.dp)
                        )

                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    if (dropSetsEnabled) {
                        OutlinedTextField(
                            value = thirdWeightText,
                            onValueChange = {
                                if (it.isNotBlank() && it.length < 6)
                                    thirdWeightText = when (it.toFloatOrNull()) {
                                        null -> thirdWeightText
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
                        Text(
                            text = stringResource(id = R.string.afterSecondDrop),
                            modifier = Modifier.padding(start = 10.dp)
                        )

                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        val newDuration = Duration.ofHours(hoursText.toLong()).plus(
                            Duration.ofMinutes(minutesText.toLong())
                        ).plus(
                            Duration.ofSeconds(secondsText.toLong())
                        )
                        val newExercise = getUpdatedExercise(
                            exercise = exercise,
                            name = nameText,
                            duration = newDuration,
                            numOfSets = numOfSetsText.toInt(),
                            numOfReps = numOfRepsText.toInt(),
                            weightOne = weightText.toFloat(),
                            weightTwo = secondWeightText.toFloat(),
                            weightThree = thirdWeightText.toFloat(),
                            dropSetsEnabled = dropSetsEnabled,
                            weightEnabled = weightEnabled,
                            imagePath = imageUri?.toString() ?: pathToDefaultIcon
                        )
                        onConfirm(newExercise)
                        onDismiss()
                    }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

private fun getUpdatedExercise(
    exercise: Exercise? = null,
    name: String,
    duration: Duration,
    numOfSets: Int,
    numOfReps: Int,
    weightOne: Float,
    weightTwo: Float,
    weightThree: Float,
    weightEnabled: Boolean,
    dropSetsEnabled: Boolean,
    imagePath: String
): Exercise {
    exercise?.let {
        return it.copy(
            name = name,
            duration = duration,
            numOfSets = numOfSets,
            repsPerSet = numOfReps,
            weightEnabled = weightEnabled,
            weightOne = weightOne,
            weightTwo = weightTwo,
            weightThree = weightThree,
            dropSetsFeatureEnabled = dropSetsEnabled,
            imagePath = imagePath
        )
    }
    return Exercise(
        name = name,
        duration = duration,
        numOfSets = numOfSets,
        repsPerSet = numOfReps,
        weightEnabled = weightEnabled,
        weightOne = weightOne,
        weightTwo = weightTwo,
        weightThree = weightThree,
        dropSetsFeatureEnabled = dropSetsEnabled,
        imagePath = imagePath
    )
}

@Preview
@Composable
fun ExerciseDialogPreview() {
    GoGymTheme {
        ExerciseDialog(
            exercise = dummyExercise,
            onDismiss = { /*TODO*/ },
            onConfirm = {
            }
        )
    }
}
