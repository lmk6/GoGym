package uk.ac.aber.dcs.cs31620.gogym.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.gogym.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.gogym.ui.theme.GoGymTheme

@Composable
fun HomeScreenTopLevel(
    navController: NavHostController
) {
    HomeScreen(navController = navController)
}

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeScreenContent(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier
) {
    Text(text = "Hello", modifier = modifier)
}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    GoGymTheme(dynamicColor = false) {
        HomeScreen(navController = navController)
    }
}
