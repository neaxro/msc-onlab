package com.example.msc_onlab.ui.feature.common

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.msc_onlab.data.model.members.Id
import com.example.msc_onlab.data.model.members.MemberData
import com.example.msc_onlab.helpers.getProfilePicture
import com.example.msc_onlab.ui.theme.MsconlabTheme
import java.lang.reflect.Member

@Composable
fun MemberDropDownMenu(
    members: List<MemberData>,
    selected: MemberData,
    onValueChange: (String) -> Unit,
    label: @Composable() (() -> Unit)? = null,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val dropDownIcon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(
        modifier = modifier.width(250.dp)
    ) {
        OutlinedTextField(
            value = getDropdownLabel(selected),
            onValueChange = {  },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .clickable {
                    expanded = !expanded
                },
            label = label,
            trailingIcon = {
                Icon(
                    dropDownIcon,
                    "DropDownArrow",
                    Modifier
                        .clickable { expanded = !expanded }
                )
            },
            readOnly = true,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            members.forEach { member ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(member._id.`$oid`)
                        expanded = false
                    },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Card(
                                modifier = Modifier.size(30.dp),
                            ) {
                                Image(
                                    painter = painterResource(id = getProfilePicture(member.profile_picture)),
                                    contentDescription = "Profile picture",
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            
                            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                            
                            Text(text = getDropdownLabel(member))
                        }
                    }
                )
            }
        }
    }
}

private fun getDropdownLabel(member: MemberData): String {
    return "${member.first_name}, ${member.last_name}"
}

@Preview(showBackground = true)
@Composable
fun MemberDropDownMenuPreview() {
    val members = listOf(
        MemberData(Id("asd111"), "neaxro@gmail.com", "Axel", "Nemes", "default", "axel"),
        MemberData(Id("asd222"), "asddsa@gmail.com", "Pete", "Smith", "man_1", "pete"),
        MemberData(Id("asd333"), "asdds@gmail.com", "Daniel", "Parker", "man_2", "daniel"),
        MemberData(Id("asd333"), "asdds@gmail.com", "Daniel", "Parker", "man_3", "daniel"),
        MemberData(Id("asd333"), "asdds@gmail.com", "Daniel", "Parker", "woman_1", "daniel"),
        MemberData(Id("asd333"), "asdds@gmail.com", "Daniel", "Parker", "woman_2", "daniel"),
    )

    var selectedMember by remember { mutableStateOf(members.first()) }

    MsconlabTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemberDropDownMenu(
                members = members,
                selected = selectedMember,
                onValueChange = {  newMemberId ->
                    val newMember = members.first { it._id.`$oid` == newMemberId }
                    selectedMember = newMember
                },
                label = { Text(text = "Select responsible") }
            )
        }

    }
}