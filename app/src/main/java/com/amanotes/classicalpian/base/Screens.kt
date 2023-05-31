package com.amanotes.classicalpian.base

sealed class Screens(val route: String) {

    object MenuScreen : Screens("menu_screen")
    object SettingsScreen : Screens("settings_screen")
    object GameScreen : Screens("game_screen")
    object ResultsScreen : Screens("results_screen")

}
