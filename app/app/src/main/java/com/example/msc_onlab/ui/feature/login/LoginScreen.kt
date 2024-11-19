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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.R
import com.example.msc_onlab.helpers.Constants
import com.example.msc_onlab.helpers.isError
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.common.SmartOutlinedTextField
import com.example.msc_onlab.ui.feature.common.SmartPasswordOutlinedTextField
import com.example.msc_onlab.ui.feature.login.LoginAction
import com.example.msc_onlab.ui.feature.login.LoginState
import com.example.msc_onlab.ui.feature.login.LoginViewModel
import com.example.msc_onlab.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccessLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    val loginData = viewModel.loginData.collectAsState().value
    val errors = viewModel.errors.collectAsState().value

    LaunchedEffect(viewModel.loginState){
        viewModel.loginState.collect{ newState ->
            if(newState is LoginState.LoggedIn){
                onSuccessLogin()
            }
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Login",
                screenState = viewModel.screenState.collectAsState()
            )
        },
        snackbarHost = {
            MySnackBarHost(screenState = viewModel.screenState)
        },
        modifier = modifier.fillMaxSize(),
    ) { padding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding() + 10.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(
                modifier = Modifier.padding(vertical = 15.dp)
            )

            Card(
                modifier = Modifier.size(160.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }

            Spacer(
                modifier = Modifier.padding(vertical = 15.dp)
            )

            // Username
            SmartOutlinedTextField(
                value = loginData.username,
                label = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Login"
                    )
                },
                onValueChange = { newUsername ->
                    viewModel.evoke(LoginAction.UpdateUsername(username = newUsername))
                },
                isError = errors.userName.isError(),
                errorMessage = errors.userName.message,
                singleLine = true,
                maxLength = Constants.MAX_USERNAME_LENGTH,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Password
            SmartPasswordOutlinedTextField(
                password = loginData.password,
                onPasswordChange = { newPassword ->
                    viewModel.evoke(LoginAction.UpdatePassword(password = newPassword))
                },
                label = {
                    Icon(
                        imageVector = Icons.Rounded.Password,
                        contentDescription = "Password"
                    )
                },
                isError = errors.password.isError(),
                errorMessage = errors.password.message,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                ),
                maxLength = Constants.MAX_PASSWORD_LENGTH
            )

            Spacer(
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Button(
                onClick = {
                    viewModel.evoke(LoginAction.Login)
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
                        navigateToRegister()
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