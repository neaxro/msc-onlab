package com.example.msc_onlab.helpers

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.msc_onlab.ui.navigation.AppScreens

data class NavigationItem(
    val id: Int = 0,
    val name: String,
    val screen: AppScreens,
    val icon: ImageVector,
    val badgeCount: Int
)
