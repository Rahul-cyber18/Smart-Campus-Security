package com.robotcar.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
) {
    object Dashboard : BottomNavItem(
        screen = Screen.Dashboard,
        icon = Icons.Default.Dashboard,
        label = "Dashboard"
    )
    
    object Control : BottomNavItem(
        screen = Screen.Control,
        icon = Icons.Default.Gamepad,
        label = "Control"
    )
    
    object Map : BottomNavItem(
        screen = Screen.Map,
        icon = Icons.Default.Map,
        label = "Map"
    )
    
    object Camera : BottomNavItem(
        screen = Screen.Camera,
        icon = Icons.Default.Videocam,
        label = "Camera"
    )
    
    object Patrol : BottomNavItem(
        screen = Screen.Patrol,
        icon = Icons.Default.Route,
        label = "Patrol"
    )
    
    object Settings : BottomNavItem(
        screen = Screen.Settings,
        icon = Icons.Default.Settings,
        label = "Settings"
    )
}
