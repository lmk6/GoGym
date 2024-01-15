package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

/**
 * Represents a top app bar component using M3 CenterAlignedTopAppBar.
 * Has a menu button icon and the app name.
 * @param onClick: provides the behaviour for the menu icon or
 * an empty lambda if not provided.
 * @author overwhelmingly Chris Loftus, I only added the:
 * @param actions Composable lambda holding a clickable icon
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {},
    title: String? = null,
    actions: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title ?: stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.nav_drawer_menu),
                    modifier = Modifier.size(128.dp)
                )
            }
        },
        actions = { actions() }
    )
}

@Preview
@Composable
private fun MainPageTopAppBarPreview() {
    GoGymTheme(dynamicColor = false) {
        MainPageTopAppBar()
    }
}