package uk.ac.aber.dcs.cs31620.gogym.ui.navigation

sealed class Screen (val route: String) {
    data object Home : Screen("home")
    data object WeekPlanner : Screen("weekPlanner")
    data object Exercises : Screen("exercises")
    data object Sessions : Screen("sessions")
    data object SessionRun : Screen("sessionRun")
    data object WorkoutView : Screen("workoutView")
}

val screens = listOf(
    Screen.Home,
    Screen.WeekPlanner,
    Screen.Exercises
)
