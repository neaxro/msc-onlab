package com.example.msc_onlab.ui.feature.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.msc_onlab.domain.wrappers.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun MySnackBarHost(
    screenState: StateFlow<ScreenState>,
    actionPerformed: () -> Unit = {},
    dismissed: () -> Unit = {},
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    SnackbarHost(hostState = snackBarHostState)

    LaunchedEffect(screenState){
        screenState.collect{ newState ->
            if(newState !is ScreenState.Loading && newState.show) {

                scope.launch {
                    val actionResult = snackBarHostState
                        .showSnackbar(
                            message = newState.message,
                            actionLabel = "Ok",
                            duration = SnackbarDuration.Short
                        )

                    when (actionResult) {
                        SnackbarResult.ActionPerformed -> {
                            actionPerformed()
                        }

                        SnackbarResult.Dismissed -> {
                            dismissed()
                        }
                    }
                }
            }
        }
    }
}
