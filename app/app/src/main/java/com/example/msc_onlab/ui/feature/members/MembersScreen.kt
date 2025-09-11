package com.example.msc_onlab.ui.feature.members

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.ui.feature.common.InvitationDialog
import com.example.msc_onlab.ui.feature.common.MemberBriefListItem
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen(
    viewModel: MembersViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val members = viewModel.members.collectAsState().value

    var showInvitationDialog by rememberSaveable { mutableStateOf(false) }

    val isFabVisible = rememberSaveable { mutableStateOf(LoggedPersonData.SELECTED_TEAM_ID != null) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Hide FAB
                if (available.y < -1) {
                    isFabVisible.value = false
                }

                // Show FAB
                if (available.y > 1 && LoggedPersonData.SELECTED_TEAM_ID != null) {
                    isFabVisible.value = true
                }

                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Members",
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
                    onClick = { showInvitationDialog = true },
                    icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = "Invite") },
                    text = { Text(text = "Invite") },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        if (members.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                state = lazyListState,
                contentPadding = PaddingValues(all = 10.dp),
            ) {
                items(members) { member ->
                    MemberBriefListItem(
                        id = member.id,
                        firstName = member.firstName,
                        lastName = member.lastName,
                        email = member.email,
                        responsibleProfilePictureName = "default"
                    )
                    if(members.last() != member){
                        HorizontalDivider(modifier = Modifier.scale(0.9f))
                    }
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
            ) {
                Text(
                    text = "Select a team first!",
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        if(showInvitationDialog){
            InvitationDialog(
                onDismissRequest = { showInvitationDialog = false },
                onConfirmation = { invitedUsername ->
                    viewModel.evoke(MembersAction.CreateInvitation(invitedUsername))
                    showInvitationDialog = false
                }
            )
        }
    }
}
