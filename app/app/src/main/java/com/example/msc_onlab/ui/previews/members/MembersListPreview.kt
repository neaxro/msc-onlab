package com.example.msc_onlab.ui.previews.members

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.msc_onlab.ui.feature.common.MemberBriefListItem
import com.example.msc_onlab.ui.theme.MsconlabTheme

@Preview(showBackground = true)
@Composable
private fun MembersListPreview() {
    MsconlabTheme {
        Column {
            MemberBriefListItem(
                id = "",
                firstName = "Axel",
                lastName = "Nemes",
                email = "neaxro@gmail.com",
                responsibleProfilePictureName = "default",
            )

            MemberBriefListItem(
                id = "",
                firstName = "Bogárka",
                lastName = "Hodászy",
                email = "boglarka.hodaszy@gmail.com",
                responsibleProfilePictureName = "woman_1",
            )
        }
    }
}
