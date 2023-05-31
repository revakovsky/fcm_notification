package com.amanotes.classicalpian.base

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amanotes.classicalpian.game.GameScreen
import com.amanotes.classicalpian.game.MenuScreen
import com.amanotes.classicalpian.game.ResultsScreen
import com.amanotes.classicalpian.game.SettingsScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MenuScreen.route) {
        composable(route = Screens.MenuScreen.route) { MenuScreen(navController = navController) }
        composable(route = Screens.SettingsScreen.route) { SettingsScreen(navController = navController) }
        composable(route = Screens.GameScreen.route) { GameScreen(navController = navController) }
        composable(route = Screens.ResultsScreen.route) { ResultsScreen(navController = navController) }
    }

}