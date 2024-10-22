package com.example.msc_onlab.ui.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.helpers.Constants
import com.example.msc_onlab.helpers.ResourceLocator
import com.example.msc_onlab.helpers.isError
import com.example.msc_onlab.ui.feature.common.HouseholdsBriefListItem
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.common.ProfilePictureSelector
import com.example.msc_onlab.ui.feature.common.SmartOutlinedTextField
import com.example.msc_onlab.ui.feature.common.SmartPasswordOutlinedTextField
import com.example.msc_onlab.ui.feature.households.CreateHouseholdDialog
import com.example.msc_onlab.ui.feature.households.DeleteHouseholdDialog
import com.example.msc_onlab.ui.feature.households.EditHouseholdDialog
import com.example.msc_onlab.ui.feature.households.HouseholdAction
import com.example.msc_onlab.ui.feature.households.HouseholdBottomSheet
import com.example.msc_onlab.ui.feature.households.HouseholdsViewModel
import com.example.msc_onlab.ui.feature.register.RegisterAction
import com.example.msc_onlab.ui.theme.Shapes

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val user = viewModel.user.collectAsState().value
    val errors = viewModel.errors.collectAsState().value

    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Profile",
                actions = {
<<<<<<< HEAD
                    IconButton(onClick = { viewModel.evoke(ProfileAction.Logout) }) {
=======
                    IconButton(onClick = {
                        viewModel.evoke(ProfileAction.Logout)
                        onLogout()
                    }) {
>>>>>>> ca5c836a75e10f0a4a5dde8480deeb8f30da2935
                        Icon(imageVector = Icons.AutoMirrored.Rounded.Logout, contentDescription = "Log out")
                    }
                }
            )
        },
        snackbarHost = {
            MySnackBarHost(screenState = viewModel.screenState)
        },
        modifier = modifier.fillMaxSize(),
    ) { padding ->
        if(user != null){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {

                Spacer(
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                Card(
                    elevation = CardDefaults.cardElevation(10.dp),
                    //border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(50.dp),
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .size(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = ResourceLocator.getProfilePicture(user.profilePicture)),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                Spacer(
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                // Firstname
                SmartOutlinedTextField(
                    value = user.firstName,
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Username"
                        )
                    },
                    onValueChange = { newFirstname ->
                        viewModel.evoke(ProfileAction.UpdateFirstname(firstname = newFirstname))
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
                    value = user.lastName,
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Lastname"
                        )
                    },
                    onValueChange = { newLastname ->
                        viewModel.evoke(ProfileAction.UpdateLastname(lastname = newLastname))
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
                    value = user.username,
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Username"
                        )
                    },
                    onValueChange = { newUsername ->
                        viewModel.evoke(ProfileAction.UpdateUsername(username = newUsername))
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
                    value = user.email,
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.Email,
                            contentDescription = "Email"
                        )
                    },
                    onValueChange = { newEmail ->
                        viewModel.evoke(ProfileAction.UpdateEmail(email = newEmail))
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

                // OldPassword
                SmartPasswordOutlinedTextField(
                    password = user.oldPassword,
                    onPasswordChange = { password ->
                        viewModel.evoke(ProfileAction.UpdateOldPassword(oldPassword = password))
                    },
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.Password,
                            contentDescription = "OldPassword"
                        )
                    },
                    isError = errors.oldPassword.isError(),
                    errorMessage = errors.oldPassword.message,
                    readOnly = false,
                    enabled = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password,
                    ),
                    maxLength = Constants.MAX_PASSWORD_LENGTH,
                    placeholder = {
                        Text(text = "Old Password")
                    },
                )

                Spacer(
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                // NewPassword
                SmartPasswordOutlinedTextField(
                    password = user.newPassword,
                    onPasswordChange = { newPassword ->
                        viewModel.evoke(ProfileAction.UpdateNewPassword(newPassword = newPassword))
                    },
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.Password,
                            contentDescription = "NewPassword"
                        )
                    },
                    isError = errors.newPassword.isError(),
                    errorMessage = errors.newPassword.message,
                    readOnly = false,
                    enabled = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password,
                    ),
                    maxLength = Constants.MAX_PASSWORD_LENGTH,
                    placeholder = {
                        Text(text = "New Password")
                    },
                )

                Spacer(
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                // NewRePassword
                SmartPasswordOutlinedTextField(
                    password = user.newRePassword,
                    onPasswordChange = { newRePassword ->
                        viewModel.evoke(ProfileAction.UpdateNewRePassword(newRePassword = newRePassword))
                    },
                    label = {
                        Icon(
                            imageVector = Icons.Rounded.Password,
                            contentDescription = "NewRePassword"
                        )
                    },
                    isError = errors.newRePassword.isError(),
                    errorMessage = errors.newRePassword.message,
                    readOnly = false,
                    enabled = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                    ),
                    maxLength = Constants.MAX_PASSWORD_LENGTH,
                    placeholder = {
                        Text(text = "New Password again")
                    },
                )

                Spacer(
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Button(
                    onClick = {
                        viewModel.evoke(ProfileAction.Update)
                    },
                    modifier = Modifier.width(250.dp),
                    shape = Shapes.small,
                    enabled = !errors.anyErrors()
                ) {
                    Text(
                        text = "Update",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.padding(vertical = 100.dp)
                )

                if(showDialog){
                    ProfilePictureSelector(
                        onDismissRequest = { showDialog = false },
                        onSelect = { newProfilePicture ->
                            viewModel.evoke(ProfileAction.UpdateProfilePicture(profilePicture = newProfilePicture))
                            showDialog = false
                        }
                    )
                }
            }
        }
        else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
            ) {
                Text(
                    text = "Profile not found...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
