package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs31620.gogym.model.workout.Workout

@Composable
fun WorkoutCard(
    modifier: Modifier = Modifier,
    workout: Workout,

    ) {
    Card (
        modifier = modifier.fillMaxWidth()
    ) {
        ConstraintLayout {
            val (imageRef, titleRef, setInfoRef, durationRef) = createRefs()


        }
    }
}