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
    object Households : AppScreens()

    @Serializable
    object Tasks : AppScreens()

    @Serializable
    object Members : AppScreens()

    @Serializable
    data class EditTask(val taskId: String) : AppScreens()

    @Serializable
    object CreateTask : AppScreens()
}
