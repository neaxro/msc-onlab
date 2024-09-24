package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc_onlab.data.model.household.HouseholdsBrief

@Composable
fun HouseholdsBriefListItem(
    title: String,
    id: String,
    numberOfMembers: Int,
    numberOfTasks: Int,
    onEdit: (String, String) -> Unit,
    onClick: (String) -> Unit,
){
    Row(
        modifier = Modifier
            .clickable { onClick(id) }
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Row {
                Text(
                    text = "Members: $numberOfMembers",
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = "Tasks: $numberOfTasks",
                    fontWeight = FontWeight.Light
                )
            }
        }

        IconButton(onClick = { onEdit(id, title) }) {
            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "Edit")
        }
    }
}