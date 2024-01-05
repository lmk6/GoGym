package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.graphics.fonts.FontStyle
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import kotlin.time.Duration

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WorkoutCard(
    modifier: Modifier = Modifier,
    workout: Workout,
    clickAction: (Workout) -> Unit = {},
    deleteAction: (Workout) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .height(80.dp)
    ) {
        ConstraintLayout {
            val (imageRef, detailsRef) = createRefs()

            GlideImage(
                model = Uri.parse(workout.imagePath),
                contentDescription = stringResource(id = R.string.workoutImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { clickAction(workout) }
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
                        end.linkTo(parent.end)
                    }
                    .padding(5.dp)
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

        val exercisesText = "${workout.exercises.size} Exercises"

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
        )

        val totalDuration = workout.exercises
            .map { it.duration }
            .reduce { accumulated, duration -> accumulated.plus(duration) }

        val durationText = "~${totalDuration.toString()}"

        Text(
            text = durationText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier
                .constrainAs(approxDurRef) {
                    start.linkTo(numOfExRef.start)
                    top.linkTo(nameRef.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}