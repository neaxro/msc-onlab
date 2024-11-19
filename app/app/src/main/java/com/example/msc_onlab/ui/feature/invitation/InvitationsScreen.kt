package com.example.msc_onlab.ui.feature.invitation

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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.ui.feature.common.InvitationCard
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.households.HouseholdAction

@Composable
fun InvitationsScreen(
    viewModel: InvitationsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.evoke(InvitationAction.LoadInvitations)
    }

    val invitations = viewModel.invitations.collectAsState().value
    if(invitations.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "You do not have any invitations.",
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    else {
        Scaffold(
            snackbarHost = {
                MySnackBarHost(screenState = viewModel.screenState)
            },
            modifier = modifier.fillMaxSize(),
        ) { padding ->
            LazyColumn(
                state = lazyListState,
                modifier = modifier
                    .fillMaxSize(),
            ) {
                items(invitations) { invitation ->
                    InvitationCard(
                        invitationData = invitation,
                        onAccept = { invitationId ->
                            viewModel.evoke(InvitationAction.AcceptInvite(invitationId))
                        },
                        onDecline = { invitationId ->
                            viewModel.evoke(InvitationAction.DeclineInvite(invitationId))
                        },
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 10.dp,
                            end = 10.dp,
                            bottom = if (invitations.last() == invitation) 10.dp else 0.dp
                        )
                    )
                }
            }
        }
    }
}