package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.day.Day
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.previewUtils.dummyDay
import uk.ac.aber.dcs.cs31620.gogym.ui.components.previewUtils.dummyWorkout
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExpandableDayCard(
    modifier: Modifier,
    day: Day,
    workout: Workout? = null,
    onChangeWorkout: (Day) -> Unit = {},
    onViewWorkoutSession: (Workout) -> Unit = {},
) {
    var expandedState by remember { mutableStateOf(true) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 90f else 0f, label = ""
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        color =
                        if (expandedState) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
            ) {
                val (imageRef, detailsRef, iconRef) = createRefs()

                val imagePath =
                    workout?.imagePath
                        ?: "file:///android_asset/images/eirik_uhlen_rest_day.jpg"
                val workoutName = workout?.name ?: stringResource(id = R.string.noSession)

                GlideImage(
                    model = Uri.parse(imagePath),
                    contentDescription = stringResource(id = R.string.workoutImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .aspectRatio(1f)
                        .constrainAs(imageRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(detailsRef.start)
                        }
                )

                DayDetails(
                    dayName = day.dayOfWeek.toString().lowercase()
                        .replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                            else it.toString()
                        },
                    workoutName = workoutName,
                    modifier = Modifier
                        .constrainAs(detailsRef) {
                            start.linkTo(imageRef.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(start = 10.dp),
                    textColour =
                        if (expandedState) MaterialTheme.colorScheme.onPrimary
                    else Color.Unspecified
                )

                IconButton(
                    modifier = Modifier
                        .constrainAs(iconRef) {
                            end.linkTo(parent.end)
                            centerVerticallyTo(parent)

                        }
                        .padding(10.dp)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = stringResource(id = R.string.dropDownArrow),
                        tint =
                            if (expandedState) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(80.dp)
                    )
                }
            }

            if (expandedState) {
                Column {
                    Text(text = "HELLO")
                }
            }
        }
    }
}

@Composable
private fun ExpandedRow() {

}

@Composable
private fun DayDetails(
    dayName: String,
    workoutName: String,
    textColour: Color,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (nameRef, numOfExRef) = createRefs()

        Text(
            text = dayName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColour,
            modifier = Modifier
                .constrainAs(nameRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(numOfExRef.top)
                }
        )

        Text(
            text = workoutName,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            color = textColour,
            modifier = Modifier
                .constrainAs(numOfExRef) {
                    start.linkTo(parent.start)
                    top.linkTo(nameRef.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 10.dp)
        )
    }
}

@Composable
@Preview
fun ExpendableDayCardPreview() {
    GoGymTheme {
        ExpandableDayCard(modifier = Modifier, day = dummyDay, workout = dummyWorkout)
    }
}

