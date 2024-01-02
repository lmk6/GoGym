package uk.ac.aber.dcs.cs31620.gogym.ui.navigation

sealed class Screen (val route: String) {
    object Home : Screen("home")
    object WeekPlanner : Screen("weekPlanner")
    object Exercises : Screen("exercises")
    object Sessions : Screen("sessions")
    object SessionView : Screen("sessionView")
    object WorkoutView : Screen("workoutView")
}

val screens = listOf(
    Screen.Home,
    Screen.WeekPlanner,
    Screen.Exercises
)
