package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc_onlab.helpers.getProfilePicture
import com.example.msc_onlab.ui.theme.MsconlabTheme

@Composable
fun SubtaskBriefListItem(
    id: String,
    isDone: Boolean,
    title: String,
    onDoneButton: (String, Boolean) -> Unit,
    onDelete: (String) -> Unit
){
    Row(
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isDone) {
                Card(
                    onClick = { onDoneButton(id, false) },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = "Done",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

            } else {
                Card(
                    onClick = { onDoneButton(id, true) },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = "Done",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.padding(horizontal = 10.dp))

            Text(text = title)
        }

        IconButton(onClick = { onDelete(id) }) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SBLI() {
    MsconlabTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubtaskBriefListItem("asdjhasgd", false, "Do the dishes", { id, isDone -> }, {})
            SubtaskBriefListItem("asdjhasgd", true, "Cook the dinner", { id, isDone -> }, {})
        }
    }
}