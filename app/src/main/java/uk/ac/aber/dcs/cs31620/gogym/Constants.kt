package uk.ac.aber.dcs.cs31620.gogym

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Few constant values used across the project
 */

const val pathToAssetImages = "file:///android_asset/images/"
const val defaultRestDayImageName = "eirik_uhlen_rest_day.jpg"
const val defaultPushUpsImageName = "push_ups_img.jpg"
const val defaultSquatImageName = "squat.jpg"

const val pathToRestDayImage = "${pathToAssetImages}${defaultRestDayImageName}"
const val pathToPushUpsImage = "${pathToAssetImages}${defaultPushUpsImageName}"
const val pathToSquatImage = "${pathToAssetImages}${defaultSquatImageName}"
const val pathToDefaultIcon = "${pathToAssetImages}default_icon.png"

val defaultRoundedCornerShape = RoundedCornerShape(20.dp)
const val MAX_NAME_LENGTH = 20
const val NUM_OF_DROP_MINI_SETS = 3
