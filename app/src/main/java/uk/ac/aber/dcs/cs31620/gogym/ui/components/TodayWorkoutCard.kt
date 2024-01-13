package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TodayWorkoutCard(
    modifier: Modifier,
    workout: Workout?,
    clickAction: (Workout) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { workout?.let(clickAction) }
            .height(180.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            val imagePath =
                workout?.imagePath ?: "file:///android_asset/images/eirik_uhlen_rest_day.jpg"

            GlideImage(
                model = Uri.parse(imagePath),
                contentDescription = stringResource(R.string.todayWorkout),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = Color.Gray, blendMode = BlendMode.Darken)
            )

            ConstraintLayout (modifier = Modifier.fillMaxSize()) {

                val (mainTextRef, textBoxRef) = createRefs()

                MainTextConstrained(
                    modifier = Modifier.constrainAs(mainTextRef) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                    },
                    workout = workout
                )
                val upperBtnString = workout?.let { stringResource(id = R.string.start) }
                    ?: stringResource(id = R.string.rest)
                val lowerBtnString = workout?.let { "~${workout.getFormattedDuration()}" }
                    ?: stringResource(id = R.string.dayTxt)

                TextBox(
                    modifier = Modifier.constrainAs(textBoxRef) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    },
                    startString = upperBtnString,
                    followupString = lowerBtnString)
            }

        }
    }

}

@Composable
fun MainTextConstrained(
    modifier: Modifier,
    workout: Workout?
) {
    ConstraintLayout(modifier = modifier.padding(start = 10.dp)) {
        val workoutName = workout?.name ?: stringResource(id = R.string.noSession)
        val (bigTextRef, workoutNameRef) = createRefs()

        Text(
            text = stringResource(id = R.string.todayWorkout),
            fontSize = 40.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .constrainAs(bigTextRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(bottom = 5.dp)
        )
        Text(
            text = workoutName,
            fontSize = 18 .sp,
            color = Color.White,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .constrainAs(workoutNameRef) {
                    start.linkTo(parent.start)
                    top.linkTo(bigTextRef.bottom)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
fun TextBox(
    modifier: Modifier,
    startString: String,
    followupString: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp))
            .background(color = Color(0x75FFFFFF))
            .height(96.dp)
            .width(150.dp),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout {
            val (bigTextRef, smallTextRef) = createRefs()

            Text(
                text = startString,
                fontSize = 40.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(bigTextRef) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }
            )

            Text(
                text = followupString,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(smallTextRef) {
                        top.linkTo(bigTextRef.bottom)
                        centerHorizontallyTo(parent)
                    }
            )
        }
    }
}

@Preview
@Composable
fun TextBoxPreview() {
    GoGymTheme(dynamicColor = false) {
        TextBox(modifier = Modifier, "START", "~20 min")
    }
}

@Preview
@Composable
fun TodayWorkoutPreview() {
    val todayWorkout = null
    GoGymTheme(dynamicColor = false) {
        TodayWorkoutCard(modifier = Modifier, workout = todayWorkout)
    }
}
