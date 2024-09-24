package com.example.msc_onlab.ui.feature.mainMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.msc_onlab.ui.feature.households.Households
import com.example.msc_onlab.ui.navigation.AppScreens

@Composable
fun MainMenuNavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    mainMenuNavController: NavHostController,
    startDestination: AppScreens = AppScreens.Households
){
    NavHost(
        navController = mainMenuNavController,
        startDestination = startDestination,
        modifier = modifier
            .fillMaxSize()
    ){
        composable<AppScreens.Households> {
            Households()
        }

        composable<AppScreens.Members> {
            Box(Modifier.fillMaxSize()) {
                Text(text = "Members", modifier.align(Alignment.Center))
            }
        }

        composable<AppScreens.Tasks> {
            Box(Modifier.fillMaxSize()) {
                Text(text = "Tasks", modifier.align(Alignment.Center))
            }
        }
    }
}