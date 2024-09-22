package com.example.msc_onlab.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppScreens {
    @Serializable
    object Login : AppScreens()

    @Serializable
    object Register : AppScreens()

    @Serializable
    object MainMenu : AppScreens()
}
