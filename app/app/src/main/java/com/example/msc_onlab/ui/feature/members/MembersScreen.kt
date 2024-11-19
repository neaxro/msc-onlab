package com.example.msc_onlab.ui.feature.members

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.ui.feature.common.DeleteDialog
import com.example.msc_onlab.ui.feature.common.HouseholdsBriefListItem
import com.example.msc_onlab.ui.feature.common.InvitationDialog
import com.example.msc_onlab.ui.feature.common.MemberBriefListItem
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.common.TaskBriefListItem
import com.example.msc_onlab.ui.feature.households.HouseholdAction
import com.example.msc_onlab.ui.feature.households.HouseholdBottomSheet

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
        if (members != null && members.data.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                state = lazyListState,
                contentPadding = PaddingValues(all = 10.dp),
            ) {
                items(members.data) { member ->
                    MemberBriefListItem(
                        id = member._id.`$oid`,
                        firstName = member.first_name,
                        lastName = member.last_name,
                        email = member.email,
                        responsibleProfilePictureName = member.profile_picture
                    )
                    if(members.data.last() != member){
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
                    text = "Select a household first!",
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
