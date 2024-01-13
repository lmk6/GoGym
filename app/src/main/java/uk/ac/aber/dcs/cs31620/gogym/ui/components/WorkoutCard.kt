package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyWorkout
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WorkoutCard(
    modifier: Modifier = Modifier,
    workout: Workout,
    clickAction: () -> Unit = {},
    deleteAction: () -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .height(80.dp)
            .clickable { clickAction() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        ConstraintLayout {
            val (imageRef, detailsRef) = createRefs()

            GlideImage(
                model = Uri.parse(workout.imagePath),
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

            DetailsSection(
                workout = workout,
                modifier = Modifier
                    .constrainAs(detailsRef) {
                        start.linkTo(imageRef.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 10.dp)
            )

        }
    }
}

@Composable
fun DetailsSection(
    workout: Workout,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (nameRef, numOfExRef, approxDurRef) = createRefs()

        Text(
            text = workout.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(nameRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(numOfExRef.top)
                }
        )
        val numOfExercises = workout.exercisesIDs.size

        val exercisesText =
            "$numOfExercises Exercise${if (numOfExercises != 1) "s" else ""}"

        Text(
            text = exercisesText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier
                .constrainAs(numOfExRef) {
                    start.linkTo(parent.start)
                    top.linkTo(nameRef.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(approxDurRef.start)
                }
                .padding(end = 10.dp)
        )

        val durationText = "~${workout.getFormattedDuration()}"

        Text(
            text = durationText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier
                .constrainAs(approxDurRef) {
                    start.linkTo(numOfExRef.end)
                    top.linkTo(nameRef.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 10.dp)
        )
    }
}

@Preview
@Composable
fun WorkoutCardPreview() {
    GoGymTheme {
        WorkoutCard(workout = dummyWorkout)
    }
}
