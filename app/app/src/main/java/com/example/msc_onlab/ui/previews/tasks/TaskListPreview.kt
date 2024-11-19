package com.example.msc_onlab.ui.previews.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc_onlab.ui.feature.common.TaskBriefListItem
import com.example.msc_onlab.ui.theme.MsconlabTheme


@Preview(showBackground = true)
@Composable
private fun TaskListPreview() {
    MsconlabTheme {
        Column {
            TaskBriefListItem(
                title = "Wash the dishes",
                id = "",
                dueDate = "2024.06.12",
                isDone = false,
                onEdit = { id, title -> },
                onClick = { id, newState -> },
                responsibleProfilePictureName = "man_1",
                responsibleName = "Nemes Axel Roland",
            )

            TaskBriefListItem(
                title = "Wash the dishes",
                id = "",
                dueDate = "2024.06.16",
                isDone = true,
                onEdit = { id, title -> },
                onClick = { id, newState -> },
                responsibleProfilePictureName = "woman_1",
                responsibleName = "Hodászy Boglárka",
            )
        }
    }
}
