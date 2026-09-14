package com.robotcar.app.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Control : Screen("control")
    object Map : Screen("map")
    object Camera : Screen("camera")
    object Patrol : Screen("patrol")
    object Settings : Screen("settings")
}
