package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Used for holding the Navigation Bar Icon details.
 * Code reused from Workshops.
 */
data class IconGroup(
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
)