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

    @Serializable
    object Teams : AppScreens()

    @Serializable
    object Tasks : AppScreens()

    @Serializable
    object Members : AppScreens()

    @Serializable
    object Profile : AppScreens()

    @Serializable
    data class EditTask(val taskId: Int) : AppScreens()

    @Serializable
    object CreateTask : AppScreens()
}
