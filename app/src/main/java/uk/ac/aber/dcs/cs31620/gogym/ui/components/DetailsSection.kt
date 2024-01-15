package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

/**
 * Composable used to get a Text section in a constrained layour
 * @param textColour colour of the text.
 * @param topText Title-like text.
 * @param bottomText Text below the topText.
 * @param extraText Optional, start where bottom text ends, below the top text.
 */
@Composable
fun DetailsSection(
    modifier: Modifier,
    textColour: Color,
    topText: String,
    bottomText: String,
    extraText: String? = null
) {
    ConstraintLayout(modifier = modifier) {
        val (topTextRef, bottomTextRef, extraTextRef) = createRefs()

        Text(
            text = topText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColour,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(topTextRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomTextRef.top)
                }
        )

        Text(
            text = bottomText,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraLight,
            color = textColour,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(bottomTextRef) {
                    start.linkTo(parent.start)
                    top.linkTo(topTextRef.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 15.dp)
        )

        extraText?.let {
            Text(
                text = extraText,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraLight,
                color = textColour,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(extraTextRef) {
                        start.linkTo(bottomTextRef.end)
                        top.linkTo(topTextRef.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(end = 10.dp)
            )
        }
    }
}