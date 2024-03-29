package uk.ac.aber.dcs.cs31620.gogym.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Creates the page scaffold to contain top app bar, navigation drawer,
 * bottom navigation button and of course the page content.
 * @param navController To pass through the NavHostController since navigation is required
 * @param pageContent So that callers can plug in their own page content.
 * By default an empty lambda.
 * @author mainly Chris Loftus, I changed only a few bits to fit my project
 */

@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    floatingActionButton: @Composable () -> Unit = { },
    snackbarContent: @Composable (SnackbarData) -> Unit = {},
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState? = null,
    title: String? = null,
    topAppBarActionContent: @Composable () -> Unit = {},
    navigationBarTopButton: @Composable () -> Unit = {},
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    MainPageNavigationDrawer(
        navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                // We know it will be open
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainPageTopAppBar(
                    onClick = {
                        coroutineScope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    },
                    title = title,
                    actions = { topAppBarActionContent() }
                )
            },
            bottomBar = {
                MainPageNavigationBar(
                    navController,
                    navigationBarTopButton
                    )
            },
            floatingActionButton = floatingActionButton,
            snackbarHost = {
                snackbarHostState?.let {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        snackbarContent(data)
                    }
                }
            },
            content = { innerPadding ->
                pageContent(innerPadding)
            }
        )
    }
}