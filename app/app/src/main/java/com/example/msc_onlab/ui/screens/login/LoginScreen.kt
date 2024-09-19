package ui.screens.login.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc_onlab.R
import com.example.msc_onlab.ui.common.SmartOutlinedTextField
import com.example.msc_onlab.ui.common.SmartPasswordOutlinedTextField
import com.example.msc_onlab.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
){
    var username by remember { mutableStateOf<String>("") }
    var password by remember { mutableStateOf<String>("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Login")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = modifier
                .padding(top = padding.calculateTopPadding() + 10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            Card(
                modifier = Modifier.size(180.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(
                modifier = Modifier.padding(vertical = 15.dp)
            )

            // Username
            SmartOutlinedTextField(
                value = username,
                label = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Login"
                    )
                },
                onValueChange = {
                    username = it
                },
                isError = false,
                singleLine = true,
                maxLength = 30,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.width(250.dp)
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Password
            SmartPasswordOutlinedTextField(
                password = password,
                onPasswordChange = {
                    password = it
                },
                label = {
                    Icon(
                        imageVector = Icons.Rounded.Password,
                        contentDescription = "Password"
                    )
                },
                isError = false,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                ),
                modifier = Modifier.width(250.dp)
            )

            Spacer(
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Button(
                onClick = {
                    username = ""
                    password = ""
                },
                modifier = Modifier.width(250.dp),
                shape = Shapes.small
            ) {
                Text(
                    text = "Login",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(250.dp)
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 12.sp
                )
                TextButton(
                    onClick = {

                    },

                    ){
                    Text(
                        text = "SIGN UP",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}