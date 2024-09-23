package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.msc_onlab.helpers.NavigationItem

@Composable
fun MyBottomNavigationBar(
    items: List<NavigationItem>,
    mainMenuNavController: NavController,
    modifier: Modifier,
    onItemClicked: (NavigationItem) -> Unit
){
    val backStackEntry = mainMenuNavController.currentBackStackEntryAsState()

    NavigationBar(
        modifier = modifier,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = false
            //val selected = item.screen.baseRoute == backStackEntry.value?.destination.

            NavigationBarItem(
                selected = selected,
                onClick = {
                    onItemClicked(item)
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if(item.badgeCount > 0){
                            BadgedBox(
                                badge = {
                                    Text(text = item.badgeCount.toString())
                                }
                            ) {
                                Icon(imageVector = item.icon, contentDescription = item.name)
                            }
                        }
                        else{
                            Icon(imageVector = item.icon, contentDescription = item.name)
                        }
                    }
                },
                label = { Text(text = item.name) },
            )
        }
    }
}
