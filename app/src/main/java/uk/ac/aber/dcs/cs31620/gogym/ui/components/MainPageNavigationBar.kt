package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.screens
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

/**
 * Code written using and according to the good practices
 * presented in workshop material, hence a nearly identical structure
 */

@Composable
fun MainPageNavigationBar(
    navController: NavController
) {
    val icons = mapOf(
        Screen.Home to IconGroup(
            filledIcon = Icons.Filled.Home,
            outlinedIcon = Icons.Outlined.Home,
            label = stringResource(id = R.string.home)
        ),
        Screen.WeekPlanner to IconGroup(
            filledIcon = Icons.Filled.CalendarViewWeek,
            outlinedIcon = Icons.Outlined.CalendarViewWeek,
            label = stringResource(id = R.string.weekPlanner)
        ),
        Screen.Exercises to IconGroup(
            filledIcon = Icons.Filled.ViewList,
            outlinedIcon = Icons.Outlined.ViewList,
            label = stringResource(id = R.string.ListOfExercises)
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val labelText = icons[screen]!!.label
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = (if (isSelected)
                            icons[screen]!!.filledIcon
                        else
                            icons[screen]!!.outlinedIcon),
                        contentDescription = labelText
                    )
                },
                label = { Text(text = labelText) }
            )
        }
    }

}

@Preview
@Composable
private fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    GoGymTheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}
