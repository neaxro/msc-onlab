package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc_onlab.data.model.household.invitation.Id
import com.example.msc_onlab.data.model.household.invitation.Invitation
import com.example.msc_onlab.data.model.household.invitation.Sender
import com.example.msc_onlab.helpers.ResourceLocator
import com.example.msc_onlab.ui.feature.login.LoginAction
import com.example.msc_onlab.ui.theme.MsconlabTheme
import com.example.msc_onlab.ui.theme.Shapes
import java.time.Instant
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun InvitationCard(
    invitationData: Invitation,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysLeftBeforeExpiration = calculateDaysUntilExpiration(invitationData.expiration_date)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
    ) {
        Box(
            modifier = Modifier
            .fillMaxWidth()
                .padding(5.dp),
        ){
            if(daysLeftBeforeExpiration > 6) {
                Canvas(
                    modifier = Modifier
                        .size(15.dp)
                ) {
                    drawCircle(Color.Red)
                }
            }

            Row(
                modifier = Modifier.padding(10.dp)
            ) {

                Card(
                    modifier = Modifier
                        .size(48.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = ResourceLocator.getProfilePicture(invitationData.sender.profile_picture)),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    Modifier.padding(start = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .height(80.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                                    append("${invitationData.sender.first_name} ${invitationData.sender.last_name}")
                                }

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Light)){
                                    append(" invited you to the household ")
                                }

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                                    append(invitationData.household_name)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.padding(vertical = 5.dp))

                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Light)){
                                    append("Expires in ")
                                }

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                                    append("$daysLeftBeforeExpiration day${if(daysLeftBeforeExpiration > 1) "s" else ""}")
                                }
                            },
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onDecline(invitationData.invitation_id) },
                            modifier = Modifier.width(120.dp),
                            shape = Shapes.small,
                        ) {
                            Text(
                                text = "Decline",
                                fontWeight = FontWeight.Normal
                            )
                        }

                        Button(
                            onClick = { onAccept(invitationData.invitation_id) },
                            modifier = Modifier.width(120.dp),
                            shape = Shapes.small
                        ) {
                            Text(
                                text = "Accept",
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun calculateDaysUntilExpiration(expirationTimestamp: Double): Int {
    // Get the current time in milliseconds
    val currentTimestamp = Instant.now().epochSecond

    // Calculate the difference between the expiration time and the current time
    val differenceInSeconds = expirationTimestamp - currentTimestamp

    // Convert seconds to days (1 day = 86400 seconds)
    val daysUntilExpiration = differenceInSeconds / 86400

    return daysUntilExpiration.roundToInt()
}

@Preview(showBackground = true)
@Composable
private fun InvitationCardPrev() {

    val data = Invitation(1730146021.630814, "Flat - Szeged", "324dh234h32h4gd324", "2346712638723", Sender(Id("asdasd232"), "neaxro@gmail.com", "Axel", "Nemes", "default", "axel"))
    val data2 = Invitation(1730006021.630814, "Flat - Szeged", "324dh234h32h4gd324", "2346712638723", Sender(Id("asdasd232"), "neaxro@gmail.com", "Axel", "Nemes", "default", "axel"))

    MsconlabTheme {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InvitationCard(
                invitationData = data,
                onAccept = {},
                onDecline = {},
                modifier = Modifier.padding(10.dp)
            )
            InvitationCard(
                invitationData = data2,
                onAccept = {},
                onDecline = {},
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}