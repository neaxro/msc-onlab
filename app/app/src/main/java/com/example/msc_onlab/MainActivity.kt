package com.example.msc_onlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.ui.feature.register.RegisterScreen
import com.example.msc_onlab.ui.navigation.MyAppNavHost
import com.example.msc_onlab.ui.theme.MsconlabTheme
import dagger.hilt.android.AndroidEntryPoint
import ui.screens.login.loginScreen.LoginScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MsconlabTheme {
                MyAppNavHost()
            }
        }
    }
}
