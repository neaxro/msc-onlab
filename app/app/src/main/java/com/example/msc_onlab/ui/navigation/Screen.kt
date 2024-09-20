package com.example.msc_onlab.ui.navigation

sealed class Screen(val baseRoute: String, val fullRoute: String = baseRoute){
    data object Login : Screen(baseRoute = "login_screen")
    data object Register : Screen(baseRoute = "register_screen")
}

fun Screen.withArgs(vararg args: String): String{
    return buildString {
        append(baseRoute)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}
