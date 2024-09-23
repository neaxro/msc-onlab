package com.example.msc_onlab.ui.feature.households

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.ui.feature.common.HouseholdsBriefListItem
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.theme.surfaceContainerHighestDark

@Composable
fun Households(
    viewModel: HouseholdsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val households = viewModel.households.collectAsState().value

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Households",
                screenState = viewModel.screenState.collectAsState()
            )
        },
        snackbarHost = {
            MySnackBarHost(screenState = viewModel.screenState)
        },
        modifier = modifier.fillMaxSize(),
    ) { padding ->
        if (households != null && households.data.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                state = lazyListState,
                contentPadding = PaddingValues(all = 10.dp)
            ) {
                items(households.data) { household ->
                    HouseholdsBriefListItem(
                        title = household.title,
                        id = household._id.`$oid`,
                        numberOfMembers = household.no_people,
                        numberOfTasks = household.no_active_tasks,
                        onEdit = { id ->
                            Toast.makeText(context, "Edited $id", Toast.LENGTH_SHORT).show()
                        },
                        onClick = { id ->
                            Toast.makeText(context, "Clicked $id", Toast.LENGTH_SHORT).show()
                        },
                    )
                    if(households.data.last() != household){
                        HorizontalDivider(modifier = Modifier.scale(0.9f))
                    }
                }
            }

        } else if (households != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
            ) {
                Text(
                    text = "You do not belong to any households yet.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
