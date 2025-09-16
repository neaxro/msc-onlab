package com.example.msc_onlab.ui.feature.common

import android.content.res.Resources.Theme
import android.graphics.Picture
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc_onlab.R
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.Responsible
import com.example.msc_onlab.helpers.ResourceLocator
import com.example.msc_onlab.ui.theme.MsconlabTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TaskBriefListItem(
    task: GetTasksResponseItem,
    onEdit: (Int, String) -> Unit,
    onClick: (Int, Boolean) -> Unit,
){
    val profilePicture = "default"

    Column (
        modifier = Modifier
            .clickable { onEdit(task.id, task.title) }
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            verticalAlignment = Alignment.Top
        ) {

            Card(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                Image(
                    painter = painterResource(id = ResourceLocator.getProfilePicture(profilePicture)),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 10.dp))

            Column (
                modifier = Modifier.padding(end = 20.dp)
            ) {

                Text(
                    text = task.title,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.padding(vertical = 3.dp))

                ListItemDetail("Responsible", "${task.responsible.firstName}, ${task.responsible.lastName}")
                ListItemDetail("Due date", task.due_date)

                Spacer(modifier = Modifier.padding(vertical = 3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatusBadge(task.status)
                    SubtaskBadge(task.subtasks.count { it.done }, task.subtasks.count())
                }

            }
        }
    }
}

@Composable
fun ListItemDetail(
    key: String,
    value: String,
    modifier: Modifier = Modifier
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = key,
            fontWeight = FontWeight.Light
        )
        Text(
            text = value,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val color = when(status.uppercase()){
        "TODO" -> Color.Gray
        "BLOCKED" -> Color.Red
        "IN PROGRESS" -> Color(0xFF2196F3)
        "IN REVIEW" -> Color(0xFFFFA000)
        "DONE" -> Color(0xFF4CAF50)
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .size(width = 125.dp, height = 25.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = color)
    ) {
        Text(
            text = status,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun SubtaskBadge(
    numDone: Int,
    numTotal: Int,
    modifier: Modifier = Modifier
) {
    val ratio = if (numTotal > 0) numDone.toFloat() / numTotal else 1f
    val color = lerp(Color.Gray, Color(0xFF4CAF50), ratio.coerceIn(0f, 1f))

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .size(width = 50.dp, height = 25.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = color)
    ) {
        Text(
            text = "$numDone / $numTotal",
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }
}



@Composable
@Preview(showBackground = true)
fun TaskBriefListItemPreview(){
    val r = Responsible(listOf(), "asd@foo.bar", true, true, "Bob", "a213l123lk213", "Anderson", 0, listOf(), true, "bob")
    val t = listOf(
        GetTasksResponseItem("Sun, 16 Nov 2025 00:00:00 GMT", "Some description", "Sun, 16 Nov 2025 00:00:00 GMT", 1, r, "TODO", subtasks = listOf(), team_id = 0, "Test Task Test Task Test TaskTest Task"),
        GetTasksResponseItem("Sun, 16 Nov 2025 00:00:00 GMT", "Some description", "Sun, 16 Nov 2025 00:00:00 GMT", 1, r, "IN PROGRESS", subtasks = listOf(), team_id = 0, "Test Task"),
        GetTasksResponseItem("Sun, 16 Nov 2025 00:00:00 GMT", "Some description", "Sun, 16 Nov 2025 00:00:00 GMT", 1, r, "DONE", subtasks = listOf(), team_id = 0, "Test Task"),
    )


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(t){
            TaskBriefListItem(
                task = it,
                onEdit = {a, b -> },
                onClick = {a, b -> }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BadgePreview(){
    val statuses = listOf(
        "TODO",
        "BLOCKED",
        "IN PROGRESS",
        "IN REVIEW",
        "DONE",
    )


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(statuses){
            StatusBadge(
                status = it,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}