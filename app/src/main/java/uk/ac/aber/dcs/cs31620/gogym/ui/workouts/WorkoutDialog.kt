package uk.ac.aber.dcs.cs31620.gogym.ui.workouts

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
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
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.pathToDefaultIcon
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyWorkout
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

/**
 * Allows Edition and Creation of a workout
 * @param workout if is not null, edition mode is on
 * otherwise, the creation starts
 * @param onConfirm on dismiss function.
 * @param onDismiss provides a new or an edited workout on confirmation.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WorkoutDialog(
    workout: Workout? = null,
    onDismiss: () -> Unit,
    onConfirm: (Workout) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
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
                        id = workout?.let { R.string.editWorkout } ?: R.string.createWorkout
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                var nameText by remember {
                    mutableStateOf(workout?.name ?: "New Workout")
                }

                var imageUri: Uri? by remember {
                    mutableStateOf(
                        Uri.parse(workout?.imagePath ?: pathToDefaultIcon)
                    )
                }
                val imagePicker = rememberLauncherForActivityResult(
                    ActivityResultContracts.PickVisualMedia()
                ) {
                    if (it != null) imageUri = it
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
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
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        val newWorkout = getUpdatedWorkout(
                            workout = workout,
                            name = nameText,
                            imagePath = imageUri?.toString() ?: pathToDefaultIcon,
                            exercisesID = workout?.exercisesIDs ?: listOf()
                        )
                        onConfirm(newWorkout)
                        onDismiss()
                    }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

private fun getUpdatedWorkout(
    workout: Workout? = null,
    name: String,
    imagePath: String,
    exercisesID: List<Long>
): Workout {
    workout?.let {
        return it.copy(
            name = name,
            imagePath = imagePath
        )
    }
    return Workout(
        name = name,
        imagePath = imagePath,
        exercisesIDs = exercisesID
    )
}

@Preview
@Composable
private fun WorkoutDialogPreview() {
    GoGymTheme {
        WorkoutDialog(dummyWorkout, onDismiss = {}, onConfirm = {})
    }
}