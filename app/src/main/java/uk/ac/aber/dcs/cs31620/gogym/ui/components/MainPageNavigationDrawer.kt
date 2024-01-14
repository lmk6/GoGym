package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.R
import uk.ac.aber.dcs.cs31620.gogym.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

/**
 * Creates the navigation drawer. Uses Material 2 since the Material 3
 * ModalNavigationDrawer is incompatible with the Material 2 Scaffold.
 * Current implementation has an image at the top and then three items.
 * @param navController To pass through the NavHostController since navigation is required
 * @param closeDrawer To pass in the close navigation drawer behaviour as a lambda.
 * By default has an empty lambda.
 * @param content To pass in the page content for the page when the navigation drawer is closed
 * @author Chris Loftus
 */
@Composable
fun MainPageNavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {

    // We are currently unable to use Material 3 ModalNavigationDrawer
    // with Material 3 Scaffold! The latter does not have a drawerContent
    // parameter forcing us to use Material 2 Scaffold.
    // Also, ModalNavigationDrawer does not play well
    // with Material 2 Scaffold (different DrawerState classes). We are however, able to use the
    // Material 3 NavigationDrawerItem, making it easier to
    // specify nav drawer clickable icons
    val items = listOf(
        Pair(
            Icons.Default.Home,
            stringResource(R.string.home)
        ),
        Pair(
            Icons.Default.ViewList,
            stringResource(R.string.ListOfSessions)
        ),
        Pair(
            Icons.Default.FormatListBulleted,
            stringResource(R.string.ListOfExercises)
        ),
        Pair(
            Icons.Default.CalendarViewWeek,
            stringResource(R.string.weekPlanner)
        )
    )
    val selectedItem = rememberSaveable { mutableIntStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Text(
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 50.dp),
                        text = stringResource(id = R.string.app_name),
                        fontSize = 35.sp
                    )

                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.first,
                                    contentDescription = item.second
                                )
                            },
                            label = { Text(item.second) },
                            // I'm not sure that having a default selected makes sense
                            selected = index == selectedItem.intValue,
                            onClick = {
                                // Set as selected.
                                selectedItem.intValue = index

                                // Just close the drawer and navigate
                                closeDrawer()
                                val route =
                                    when (index) {
                                        0 -> Screen.Home.route
                                        1 -> Screen.Sessions.route
                                        2 -> Screen.Exercises.route
                                        3 -> Screen.WeekPlanner.route
                                        else -> null
                                    }
                                route?.let {
                                    navController.navigate(route = route) {
                                        popUpTo(navController.graph.findStartDestination().id)
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        content = content
    )
}

@Preview
@Composable
private fun MainPageNavigationDrawerPreview() {
    GoGymTheme(dynamicColor = false) {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        MainPageNavigationDrawer(navController, drawerState)
    }
}