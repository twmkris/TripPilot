
package com.vibecoding.trippilot.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vibecoding.trippilot.presentation.add_trip.AddTripScreen
import com.vibecoding.trippilot.presentation.trip_list.TripListScreen
import com.vibecoding.trippilot.presentation.trip_detail.TripDetailScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController, 
        startDestination = Screen.TripListScreen.route
    ) {
        composable(route = Screen.TripListScreen.route) {
            TripListScreen(navController = navController)
        }
        composable(route = Screen.AddTripScreen.route) {
            AddTripScreen(navController = navController)
        }
        composable(
            route = Screen.TripDetailScreen.route,
            arguments = listOf(
                navArgument("tripId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            TripDetailScreen(navController = navController)
        }
    }
}

sealed class Screen(val route: String) {
    object TripListScreen : Screen("trip_list_screen")
    object AddTripScreen : Screen("add_trip_screen")
    object TripDetailScreen : Screen("trip_detail_screen/{tripId}")
}
