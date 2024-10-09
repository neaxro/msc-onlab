package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc_onlab.ui.theme.MsconlabTheme

@Composable
fun AddNewSubtaskItem(
    title: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.secondaryContainer),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ANSI_low() {
    MsconlabTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AddNewSubtaskItem(  "Add",  {})
            SubtaskCreateListItem(0,  "Do the dishes",  {})
            SubtaskCreateListItem(1,  "Cook the dinner",  {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ANSI() {
    MsconlabTheme {

        Scaffold(
            topBar = {
                MyTopAppBar(title = "Task")
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddNewSubtaskItem(  "New subtask",  {})
                SubtaskCreateListItem(0,  "Do the dishes",  {})
                SubtaskCreateListItem(1,  "Cook the dinner",  {})
            }
        }
    }
}