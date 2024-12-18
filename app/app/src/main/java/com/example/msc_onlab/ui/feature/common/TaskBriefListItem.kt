package com.example.msc_onlab.ui.feature.common

import android.content.res.Resources.Theme
import android.graphics.Picture
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc_onlab.R
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.helpers.ResourceLocator
import com.example.msc_onlab.ui.theme.MsconlabTheme

@Composable
fun TaskBriefListItem(
    title: String,
    id: String,
    responsibleProfilePictureName: String,
    responsibleName: String,
    dueDate: String,
    isDone: Boolean,
    onEdit: (String, String) -> Unit,
    onClick: (String, Boolean) -> Unit,
){
    Row(
        modifier = Modifier
            .clickable { onEdit(id, title) }
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                Image(
                    painter = painterResource(id = ResourceLocator.getProfilePicture(responsibleProfilePictureName)),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 10.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.padding(vertical = 3.dp))
                Text(
                    text = responsibleName,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.padding(vertical = 3.dp))
                Text(
                    text = "Due date: $dueDate",
                    fontWeight = FontWeight.ExtraLight
                )
            }
        }

        if(isDone){
            Card(
                onClick = { onClick(id, false) },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(imageVector = Icons.Rounded.Done, contentDescription = "Done", tint = MaterialTheme.colorScheme.onPrimary)
            }

        }
        else{
            Card(
                onClick = { onClick(id, true) },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Rounded.Done, contentDescription = "Done", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
