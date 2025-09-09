package com.example.msc_onlab.ui.feature.mainMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.msc_onlab.helpers.NavigationItem
import com.example.msc_onlab.ui.feature.common.MyBottomNavigationBar
import com.example.msc_onlab.ui.navigation.AppScreens

@Composable
fun MainMenu(
    logout: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val mainMenuNavController = rememberNavController()

    val bottomNavigationItems = listOf(
        NavigationItem(0, "Teams", AppScreens.Teams, Icons.Rounded.Groups, 0),
        NavigationItem(0, "Tasks", AppScreens.Tasks, Icons.Rounded.TaskAlt, 0),
        NavigationItem(0, "Members", AppScreens.Members, Icons.Rounded.PeopleAlt, 0),
        NavigationItem(0, "Profile", AppScreens.Profile, Icons.Rounded.Person, 0),
    )

    Scaffold(
        bottomBar = {
            MyBottomNavigationBar(
                items = bottomNavigationItems,
                mainMenuNavController = mainMenuNavController,
                modifier = Modifier,
                onItemClicked = { nextScreen ->
                    mainMenuNavController.navigate(nextScreen.screen)
                }
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ){
            MainMenuNavHost(
                navController = navController,
                mainMenuNavController = mainMenuNavController
            )
        }
    }
}