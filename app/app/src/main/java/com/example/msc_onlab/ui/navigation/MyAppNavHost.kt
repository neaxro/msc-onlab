package com.example.msc_onlab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.msc_onlab.ui.feature.register.RegisterScreen
import ui.screens.login.loginScreen.LoginScreen
import kotlin.reflect.KClass

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AppScreens = AppScreens.Login
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable<AppScreens.Login> {
            LoginScreen(
                onSuccessLogin = { /*TODO*/ },
                navigateToRegister = {
                    navController.navigate(AppScreens.Register)
                }
            )
        }

        composable<AppScreens.Register> {
            RegisterScreen(
                onSuccessRegister = { /*TODO*/ },
                navigateToLogin = {
                    navController.navigate(AppScreens.Login)
                }
            )
        }
    }
}
