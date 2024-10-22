package com.example.msc_onlab.ui.feature.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.helpers.Constants
import com.example.msc_onlab.helpers.isError
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.common.SmartOutlinedTextField
import com.example.msc_onlab.ui.feature.common.SmartPasswordOutlinedTextField
import com.example.msc_onlab.ui.feature.login.LoginAction
import com.example.msc_onlab.ui.theme.Shapes

@Composable
fun RegisterScreen(
    onSuccessRegister: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val registerData = viewModel.registerData.collectAsState().value
    val errors = viewModel.errors.collectAsState().value
    val rePassword = viewModel.rePassword.collectAsState().value

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Register",
                screenState = viewModel.screenState.collectAsState(),
            )
        },
        snackbarHost = {
            MySnackBarHost(screenState = viewModel.screenState)
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Firstname
            SmartOutlinedTextField(
                value = registerData.first_name,
                label = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Username"
                    )
                },
                onValueChange = { newFirstname ->
                    viewModel.evoke(RegisterAction.UpdateFirstname(firstname = newFirstname))
                },
                isError = errors.firstname.isError(),
                errorMessage = errors.firstname.message,
                singleLine = true,
                maxLength = Constants.MAX_FIRSTNAME_LENGTH,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                placeholder = {
                    Text(text = "First name")
                },
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Lastname
            SmartOutlinedTextField(
                value = registerData.last_name,
                label = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Lastname"
                    )
                },
                onValueChange = { newLastname ->
                    viewModel.evoke(RegisterAction.UpdateLastname(lastname = newLastname))
                },
                isError = errors.lastname.isError(),
                errorMessage = errors.lastname.message,
                singleLine = true,
                maxLength = Constants.MAX_LASTNAME_LENGTH,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                placeholder = {
                    Text(text = "Last name")
                },
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Username
            SmartOutlinedTextField(
                value = registerData.username,
                label = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Username"
                    )
                },
                onValueChange = { newUsername ->
                    viewModel.evoke(RegisterAction.UpdateUsername(username = newUsername))
                },
                isError = errors.username.isError(),
                errorMessage = errors.username.message,
                singleLine = true,
                maxLength = Constants.MAX_USERNAME_LENGTH,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                placeholder = {
                    Text(text = "User name")
                },
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Email
            SmartOutlinedTextField(
                value = registerData.email,
                label = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email"
                    )
                },
                onValueChange = { newEmail ->
                    viewModel.evoke(RegisterAction.UpdateEmail(email = newEmail))
                },
                isError = errors.email.isError(),
                errorMessage = errors.email.message,
                singleLine = true,
                maxLength = Constants.MAX_EMAIL_LENGTH,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                placeholder = {
                    Text(text = "Email")
                },
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // Password
            SmartPasswordOutlinedTextField(
                password = registerData.password,
                onPasswordChange = { newPassword ->
                    viewModel.evoke(RegisterAction.UpdatePassword(password = newPassword))
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
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password,
                ),
                maxLength = Constants.MAX_PASSWORD_LENGTH,
                placeholder = {
                    Text(text = "Password")
                },
            )

            Spacer(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // RePassword
            SmartPasswordOutlinedTextField(
                password = rePassword,
                onPasswordChange = { newRePassword ->
                    viewModel.evoke(RegisterAction.UpdateRePassword(rePassword = newRePassword))
                },
                label = {
                    Icon(
                        imageVector = Icons.Rounded.Password,
                        contentDescription = "RePassword"
                    )
                },
                isError = errors.rePassword.isError(),
                errorMessage = errors.rePassword.message,
                readOnly = false,
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                ),
                maxLength = Constants.MAX_PASSWORD_LENGTH,
                placeholder = {
                    Text(text = "Password again")
                },
            )

            Spacer(
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Button(
                onClick = {
                    viewModel.evoke(RegisterAction.Register)
                },
                modifier = Modifier.width(250.dp),
                shape = Shapes.small,
            ) {
                Text(
                    text = "Register",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(250.dp)
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 12.sp
                )
                TextButton(
                    onClick = {
                        navigateToLogin()
                    },

                    ){
                    Text(
                        text = "SIGN IN",
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 80.dp))
        }
    }
}
