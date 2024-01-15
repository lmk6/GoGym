package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.RemoveRedEye
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.defaultRoundedCornerShape
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyDay
import uk.ac.aber.dcs.cs31620.gogym.ui.components.utils.dummyWorkout
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme
import java.util.Locale

/**
 * Expandable Card Composable featuring two custom buttons and the details section.
 * @param imagePath String path to an image displayed on top start.
 * @param topText Title-like Text-String.
 * @param topButtonText Top button Text.
 * @param topButtonImageVector Top Button Icon.
 * @param bottomText Description text below the top text.
 * @param bottomButtonText Bottom button Text.
 * @param bottomButtonImageVector Bottom Button Icon.
 * @param extraText extra text starting where bottom text ends.
 * @param onClickTopButton lambda for top button on click action.
 * @param onClickBottomButton lambda for bottom button on click action.
 * @param detailsList List of Strings that will be displayed in between the
 * main section and custom buttons. Text will be organized into two columns.
 * One text goes for a column.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExpandableCard(
    modifier: Modifier,
    imagePath: String,
    topText: String,
    bottomText: String,
    extraText: String? = null,
    topButtonText: String,
    bottomButtonText: String? = null,
    topButtonImageVector: ImageVector,
    bottomButtonImageVector: ImageVector? = null,
    onClickTopButton: () -> Unit = {},
    onClickBottomButton: () -> Unit = {},
    detailsList: List<String>? = null
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 90f else 0f, label = ""
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(defaultRoundedCornerShape)
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
                    topText = topText,
                    bottomText = bottomText,
                    extraText = extraText,
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

                detailsList?.let {
                    ExpandedDetails(detailsList)
                }

                ActionButton(
                    actionDescription = topButtonText,
                    iconImage = topButtonImageVector,
                    iconDescription = topButtonText,
                    onClick = onClickTopButton
                )

                if (bottomButtonText != null && bottomButtonImageVector != null)
                    ActionButton(
                        actionDescription = bottomButtonText,
                        iconImage = bottomButtonImageVector,
                        iconDescription = bottomButtonText,
                        onClick = onClickBottomButton
                    )
            }
        }
    }
}

@Composable
private fun ExpandedDetails(
    detailsList: List<String>
) {
    val rowHeight = 36  // estimated Row Height to prevent the infinity maximum height constraints error
    val fullHeight = ((detailsList.size + 1) / 2) * rowHeight
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp).height(fullHeight.dp)
    ) {
        items(detailsList) { detail ->
            Text(
                detail,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    start = 15.dp,
                    bottom = 10.dp,
                    end = 15.dp
                )
            )
        }
    }
}

/**
 * Provides Action Buttons when the Card is expanded
 */
@Composable
private fun ActionButton(
    actionDescription: String,
    iconImage: ImageVector,
    iconDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = Color.Transparent
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(10.dp)
                .padding(start = 15.dp)
        ) {
            Icon(
                imageVector = iconImage,
                contentDescription = iconDescription,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = actionDescription,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview
fun ExpendableCardPreview() {
    val topText = dummyDay.dayOfWeek.toString().lowercase()
        .replaceFirstChar { itDay ->
            if (itDay.isLowerCase()) itDay.titlecase(Locale.getDefault())
            else itDay.toString()
        }

    GoGymTheme {
        ExpandableCard(
            modifier = Modifier,
            topText = topText,
            bottomText = dummyWorkout.name,
            imagePath = dummyWorkout.imagePath,
            topButtonImageVector = Icons.Rounded.ChangeCircle,
            topButtonText = stringResource(id = R.string.changeWorkout),
            bottomButtonImageVector = Icons.Rounded.RemoveRedEye,
            bottomButtonText = stringResource(id = R.string.viewWorkout),
            detailsList = listOf("DummyData1", "DummyData2", "DummyData3")
        )
    }
}

