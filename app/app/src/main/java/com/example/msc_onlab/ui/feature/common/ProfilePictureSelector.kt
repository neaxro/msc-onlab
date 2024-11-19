package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.window.Dialog
import com.example.msc_onlab.helpers.ResourceLocator
import com.example.msc_onlab.ui.theme.MsconlabTheme
import com.example.msc_onlab.ui.theme.Shapes

@Composable
fun ProfilePictureSelector(
    onDismissRequest: () -> Unit,
    onSelect: (String) -> Unit,
){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            tonalElevation = 5.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(ResourceLocator.availableProfilePictures){ profilePictureId ->
                    Image(
                        painter = painterResource(id = ResourceLocator.getProfilePicture(profilePictureId)),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clickable {
                            onSelect(profilePictureId)
                        }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PPSP() {

    var profilePicture by rememberSaveable { mutableStateOf("man_1") }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    MsconlabTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()){
                Card(
                    elevation = CardDefaults.cardElevation(10.dp),
                    //border = BorderStroke(0.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(50.dp),
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = ResourceLocator.getProfilePicture(profilePicture)),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                if(showDialog){
                    ProfilePictureSelector(
                        onDismissRequest = { showDialog = false },
                        onSelect = { newProfilePictureId ->
                            profilePicture = newProfilePictureId
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
}