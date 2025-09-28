package com.example.msc_onlab.ui.feature.teams

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.ui.feature.common.TeamBriefListItem
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.invitation.InvitationsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Teams(
    viewModel: TeamsViewModel = hiltViewModel(),
    onNavigateToTasks: () -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val teams = viewModel.teams.collectAsState().value
    val teamActionData = viewModel.teamActionData.collectAsState().value

    var selectedTabIndex by rememberSaveable { mutableStateOf<TeamsPage>(TeamsPage.Teams) }

    // Bottom Sheet
    val sheetState = rememberModalBottomSheetState()

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
                title = "Teams",
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
                    onClick = { viewModel.evoke(TeamAction.ShowCreateDialog) },
                    icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = "Create team") },
                    text = { Text(text = "New team") },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TabRow(selectedTabIndex = selectedTabIndex.ordinal) {
                TeamsPage.entries.forEachIndexed() { index, tabPage ->
                    Tab(
                        selected = index == selectedTabIndex.ordinal,
                        onClick = { selectedTabIndex = tabPage },
                        text = { Text(text = tabPage.title) },
                    )
                }
            }

            when(selectedTabIndex){
                TeamsPage.Teams -> {
                    viewModel.evoke(TeamAction.LoadTeams)
                    if (teams != null && teams.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedScrollConnection),
                        state = lazyListState,
                        contentPadding = PaddingValues(all = 10.dp),
                    ) {
                        items(teams) { team ->
                            TeamBriefListItem(
                                title = team.name,
                                id = team.id,
                                description = team.description,
                                onEdit = { id, title ->
                                    viewModel.evoke(TeamAction.ShowSheet(id = id, title = title))
                                },
                                onClick = { id ->
                                    viewModel.evoke(TeamAction.SelectTeam(id))
                                    onNavigateToTasks()
                                }
                            )
                            if(teams.last() != team){
                                HorizontalDivider(modifier = Modifier.scale(0.9f))
                            }
                        }
                    }
                    } else if (teams != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = padding.calculateTopPadding())
                        ) {
                            Text(
                                text = "You do not belong to any teams yet.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                TeamsPage.Invitations -> {
                    isFabVisible.value = false
                    InvitationsScreen()
                }
            }
        }


        if(teamActionData.showEditDialog){
            EditTeamDialog(
                currentName = teamActionData.title,
                currentDescription = teamActionData.description,
                onDismissRequest = { viewModel.evoke(TeamAction.HideEditDialog) },
                onConfirmation = { newName, newDescription ->
                    viewModel.evoke(TeamAction.EditTeam(newName, newDescription))
                }
            )
        }

        if(teamActionData.showCreateDialog){
            CreateTeamDialog(
                onDismissRequest = {
                    viewModel.evoke(TeamAction.HideCreateDialog)
                },
                onConfirmation = { title ->
                    viewModel.evoke(TeamAction.CreateTeam(title))
                }
            )
        }

        if(teamActionData.showDeleteDialog){
            DeleteTeamDialog(
                title = teamActionData.title,
                onDismissRequest = { viewModel.evoke(TeamAction.HideDeleteDialog) },
                onConfirmation = { viewModel.evoke(TeamAction.DeleteTeam) }
            )
        }

        if(teamActionData.showSheet){
            TeamBottomSheet(
                householdTitle = teamActionData.title,
                onDismissRequest = { viewModel.evoke(TeamAction.HideSheet) },
                sheetState = sheetState,
                onEdit = { viewModel.evoke(TeamAction.ShowEditDialog) },
                onDelete = { viewModel.evoke(TeamAction.ShowDeleteDialog) },
            )
        }
    }
}

private enum class TeamsPage(val title: String, val icon: ImageVector){
    Teams("Teams", Icons.Rounded.TaskAlt),
    Invitations("Invitations", Icons.Rounded.Edit),
}
