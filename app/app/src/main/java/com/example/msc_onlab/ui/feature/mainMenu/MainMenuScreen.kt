package com.example.msc_onlab.ui.feature.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.House
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.helpers.NavigationItem
import com.example.msc_onlab.ui.feature.common.MyBottomNavigationBar
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.login.LoginState
import com.example.msc_onlab.ui.navigation.AppScreens

/*
@Composable
fun MainMenu(
    logout: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Main Menu",
                actions = {
                    IconButton(onClick = { logout() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Logout,
                            contentDescription = "Log out"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            Text(text = "ID: ${LoggedPersonData.ID}")
            Text(text = "USERNAME: ${LoggedPersonData.USERNAME}")
        }
    }
}*/

@Composable
fun MainMenu(
    logout: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val mainMenuNavController = rememberNavController()

    val bottomNavigationItems = listOf(
        NavigationItem(0, "Households", AppScreens.Households, Icons.Rounded.House, 0),
        NavigationItem(0, "Tasks", AppScreens.Tasks, Icons.Rounded.TaskAlt, 0),
        NavigationItem(0, "Members", AppScreens.Members, Icons.Rounded.PeopleAlt, 0),
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
                .background(Color.LightGray)
                .padding(bottom = padding.calculateBottomPadding())
        ){
            MainMenuNavHost(
                navController = navController,
                mainMenuNavController = mainMenuNavController
            )
        }
    }
}