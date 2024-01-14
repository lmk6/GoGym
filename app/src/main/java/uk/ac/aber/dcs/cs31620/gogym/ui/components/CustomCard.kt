package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.defaultRoundedCornerShape
import uk.ac.aber.dcs.cs31620.gogym.pathToPushUpsImage
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomCard(
    modifier: Modifier,
    imagePath: String,
    topText: String,
    bottomText: String,
    extraText: String? = null,
    clickAction: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(defaultRoundedCornerShape)
            .height(80.dp)
            .shadow(4.dp, shape = defaultRoundedCornerShape)
            .clickable { clickAction() },
    ) {
        ConstraintLayout {
            val (imageRef, detailsRef) = createRefs()

            GlideImage(
                model = Uri.parse(imagePath),
                contentDescription = stringResource(id = R.string.workoutImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(defaultRoundedCornerShape)
                    .aspectRatio(1f)
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(detailsRef.start)
                    }
            )

            DetailsSection(
                modifier = Modifier
                    .constrainAs(detailsRef) {
                        start.linkTo(imageRef.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 10.dp),
                textColour = Color.Unspecified,
                topText = topText,
                bottomText = bottomText,
                extraText = extraText
            )
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    GoGymTheme {
        CustomCard(
            modifier = Modifier,
            imagePath = pathToPushUpsImage,
            topText = "Push Ups",
            bottomText = "3 Exercises",
            extraText = "~20 min"
        )
    }
}
