package com.example.msc_onlab.ui.feature.households

import android.graphics.drawable.shapes.Shape
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.ui.feature.common.HouseholdsBriefListItem
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.theme.Shapes
import com.example.msc_onlab.ui.theme.surfaceContainerHighestDark

@Composable
fun Households(
    viewModel: HouseholdsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val households = viewModel.households.collectAsState().value
    val editHouseholdData = viewModel.editHouseholdData.collectAsState().value

    val isFabVisible = rememberSaveable { mutableStateOf(true) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Hide FAB
                if (available.y < -1) {
                    isFabVisible.value = false
                }

                // Show FAB
                if (available.y > 1) {
                    isFabVisible.value = true
                }

                return Offset.Zero
            }
        }
    }

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
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFabVisible.value,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {},
                    icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = "Create Household") },
                    text = { Text(text = "New household") },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        if (households != null && households.data.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .nestedScroll(nestedScrollConnection),
                state = lazyListState,
                contentPadding = PaddingValues(all = 10.dp),
            ) {
                items(households.data) { household ->
                    HouseholdsBriefListItem(
                        title = household.title,
                        id = household._id.`$oid`,
                        numberOfMembers = household.no_people,
                        numberOfTasks = household.no_active_tasks,
                        onEdit = { id, title ->
                            viewModel.evoke(HouseholdAction.SetIdEditData(id))
                            viewModel.evoke(HouseholdAction.SetCurrentNameEditData(title))
                            viewModel.evoke(HouseholdAction.ShowEditDialog)
                        },
                        onClick = { id ->
                            viewModel.evoke(HouseholdAction.SelectHousehold(id))
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

        if(editHouseholdData.showDialog){
            EditHouseholdDialog(
                currentName = editHouseholdData.currentName,
                onDismissRequest = { viewModel.evoke(HouseholdAction.HideEditDialog) },
                onConfirmation = { newName ->
                    viewModel.evoke(HouseholdAction.SetNewNameEditData(newName))
                }
            )
        }
    }
}
