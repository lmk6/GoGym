package uk.ac.aber.dcs.cs31620.gogym.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    navController: NavController,
    onTopOfComposable: @Composable () -> Unit = {}
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        onTopOfComposable()

        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination


            screens.forEach { screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                val labelText = icons[screen]!!.label
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
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

}


@Preview
@Composable
private fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    GoGymTheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}
