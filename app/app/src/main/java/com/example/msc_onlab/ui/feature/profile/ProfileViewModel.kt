package com.example.msc_onlab.ui.feature.profile

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.Data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.profile.Id
import com.example.msc_onlab.data.model.profile.ProfileData
import com.example.msc_onlab.data.model.profile.UpdateProfileData
import com.example.msc_onlab.data.repository.login.LoginRepository
import com.example.msc_onlab.data.repository.profile.ProfileRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.AppDataRememberer
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.helpers.clear
import com.example.msc_onlab.helpers.or
import com.example.msc_onlab.helpers.sha256
import com.example.msc_onlab.helpers.validateFirstname
import com.example.msc_onlab.helpers.validateLastname
import com.example.msc_onlab.helpers.validatePasswordsMatch
import com.example.msc_onlab.helpers.validateUserEmail
import com.example.msc_onlab.helpers.validateUserPassword
import com.example.msc_onlab.helpers.validateUsername
import com.example.msc_onlab.ui.feature.register.RegisterFieldErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val loginRepository: LoginRepository,
    private val sharedPreferences: SharedPreferences,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _user = MutableStateFlow<UpdateProfileTempData?>(null)
    val user = _user.asStateFlow()

    private val _errors = MutableStateFlow<ProfileFieldErrors>(ProfileFieldErrors())
    val errors = _errors.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = profileRepository.getProfile(LoggedPersonData.ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    _user.value = result.data!!.data.toTempData()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun updateProfile(updateData: UpdateProfileData){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = profileRepository.updateProfile(updateData)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success(show = true)

                    AppDataRememberer.rememberLoginData(
                        sharedPreferences = sharedPreferences,
                        username = _user.value!!.username,
                        password = _user.value!!.newPassword,
                    )

                    // Update the current token
                    LoggedPersonData.TOKEN = result.data!!.data
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun logout(){
        LoggedPersonData.clear()
        AppDataRememberer.forgetLoginData(sharedPreferences)
    }

    fun evoke(action: ProfileAction){
        when(action){
            is ProfileAction.UpdateEmail -> {
                _user.update {
                    it?.copy(
                        email = action.email
                    )
                }

                _errors.update {
                    it.copy(
                        email = validateUserEmail(action.email, applicationContext)
                    )
                }
            }
            is ProfileAction.UpdateFirstname -> {
                _user.update {
                    it?.copy(
                        firstName = action.firstname
                    )
                }

                _errors.update {
                    it.copy(
                        firstname = validateFirstname(action.firstname, applicationContext)
                    )
                }
            }
            is ProfileAction.UpdateLastname -> {
                _user.update {
                    it?.copy(
                        lastName = action.lastname
                    )
                }

                _errors.update {
                    it.copy(
                        lastname = validateLastname(action.lastname, applicationContext)
                    )
                }
            }
            is ProfileAction.UpdateNewPassword -> {
                _user.update {
                    it?.copy(
                        newPassword = action.newPassword
                    )
                }

                _errors.update {
                    it.copy(
                        newPassword = validateUserPassword(action.newPassword, applicationContext),
                    )
                }
            }

            is ProfileAction.UpdateNewRePassword -> {
                _user.update {
                    it?.copy(
                        newRePassword = action.newRePassword
                    )
                }

                _errors.update {
                    val passwordCheckResult = validateUserPassword(action.newRePassword.trim(), applicationContext)
                    val error = if(passwordCheckResult !is DataFieldErrors.NoError){
                            passwordCheckResult
                        }
                        else{
                            validatePasswordsMatch(
                                password = _user.value!!.newPassword,
                                passwordChange = _user.value!!.newRePassword,
                                context = applicationContext
                            )
                        }

                    it.copy(
                        newRePassword = error
                    )
                }
            }
            is ProfileAction.UpdateOldPassword -> {
                _user.update {
                    it?.copy(
                        oldPassword = action.oldPassword
                    )
                }

                _errors.update {
                    it.copy(
                        oldPassword = validateUserPassword(action.oldPassword, applicationContext),
                    )
                }
            }
            is ProfileAction.UpdateUsername -> {
                _user.update {
                    it?.copy(
                        username = action.username
                    )
                }

                _errors.update {
                    it.copy(
                        username = validateUsername(action.username, applicationContext),
                    )
                }
            }

            is ProfileAction.UpdateProfilePicture -> {
                _user.update {
                    it?.copy(
                        profilePicture = action.profilePicture
                    )
                }
            }

            is ProfileAction.Update -> {
                checkAllValues()
                if(_errors.value.anyErrors()) return

                checkCurrentPassword(_user.value!!.oldPassword) { matching ->
                    if (matching) {
                        val isPasswordChane = _user.value!!.newPassword.isNotEmpty()
                        val updateData = _user.value!!.toUpdateData(isPasswordChane)

                        // Update the user's data
                        updateProfile(updateData)
                    }
                }
            }

            ProfileAction.Logout -> {
                logout()
            }
        }
    }

    private fun checkCurrentPassword(typedOldPassword: String, onResult: (Boolean) -> Unit) {
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = loginRepository.loginPerson(
                LoginData(
                    username = LoggedPersonData.USERNAME!!,
                    password = typedOldPassword.sha256()
                )
            )

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    onResult(true)
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = "Wrong old password!")
                    onResult(false)
                }
            }
        }
    }

    private fun checkAllValues(){
        val firstnameCheck = validateFirstname(_user.value!!.firstName, applicationContext)
        val lastnameCheck = validateLastname(_user.value!!.lastName, applicationContext)
        val usernameCheck = validateUsername(_user.value!!.username, applicationContext)
        val emailCheck = validateUserEmail(_user.value!!.email, applicationContext)
        val oldPasswordCheck = validateUserPassword(_user.value!!.oldPassword, applicationContext)
        val newPasswordCheck = validateUserPassword(_user.value!!.newPassword, applicationContext)
        val newRePasswordCheck = validateUserPassword(_user.value!!.newRePassword, applicationContext)
        val passwordMachCheck = validatePasswordsMatch(_user.value!!.newPassword, _user.value!!.newRePassword, applicationContext)

        // User does not want to change password
        if( _user.value!!.newPassword.isEmpty() &&
            _user.value!!.newRePassword.isEmpty()
            ){
            _errors.update {
                it.copy(
                    firstname = firstnameCheck,
                    lastname = lastnameCheck,
                    username = usernameCheck,
                    email = emailCheck,
                    oldPassword = passwordMachCheck,
                    newPassword = DataFieldErrors.NoError,
                    newRePassword = DataFieldErrors.NoError
                )
            }
        }
        // User wants to change password
        else{
            _errors.update {
                it.copy(
                    firstname = firstnameCheck,
                    lastname = lastnameCheck,
                    username = usernameCheck,
                    email = emailCheck,
                    oldPassword = oldPasswordCheck,
                    newPassword = newPasswordCheck,
                    newRePassword = newRePasswordCheck
                )
            }
        }
    }
}

sealed class ProfileAction{
    object Update : ProfileAction()
    object Logout : ProfileAction()
    class UpdateFirstname(val firstname: String) : ProfileAction()
    class UpdateLastname(val lastname: String) : ProfileAction()
    class UpdateUsername(val username: String) : ProfileAction()
    class UpdateEmail(val email: String) : ProfileAction()
    class UpdateProfilePicture(val profilePicture: String) : ProfileAction()
    class UpdateOldPassword(val oldPassword: String) : ProfileAction()
    class UpdateNewPassword(val newPassword: String) : ProfileAction()
    class UpdateNewRePassword(val newRePassword: String) : ProfileAction()
}

data class ProfileFieldErrors(
    val firstname: DataFieldErrors = DataFieldErrors.NoError,
    val lastname: DataFieldErrors = DataFieldErrors.NoError,
    val username: DataFieldErrors = DataFieldErrors.NoError,
    val email: DataFieldErrors = DataFieldErrors.NoError,
    val profilePicture: DataFieldErrors = DataFieldErrors.NoError,
    val oldPassword: DataFieldErrors = DataFieldErrors.PasswordError(""),
    val newPassword: DataFieldErrors = DataFieldErrors.NoError,
    val newRePassword: DataFieldErrors = DataFieldErrors.NoError,
)

fun ProfileFieldErrors.anyErrors(): Boolean {
    val error = this.firstname
        .or(this.lastname)
        .or(this.username)
        .or(this.email)
        .or(this.profilePicture)
        .or(this.oldPassword)
        .or(this.newPassword)
        .or(this.newRePassword)

    return error !is DataFieldErrors.NoError
}

data class UpdateProfileTempData(
    val id: Id,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePicture: String,
    val username: String,
    val oldPassword: String,
    val newPassword: String,
    val newRePassword: String
)

private fun ProfileData.toTempData(): UpdateProfileTempData{
    return UpdateProfileTempData(
        id = this._id,
        email = this.email,
        firstName = this.first_name,
        lastName = this.last_name,
        profilePicture = this.profile_picture,
        username = this.username,
        oldPassword = "",
        newPassword = "",
        newRePassword = ""
    )
}

private fun UpdateProfileTempData.toUpdateData(isPasswordChange: Boolean): UpdateProfileData {
    return UpdateProfileData(
        _id = this.id,
        username = this.username,
        email = this.email,
        first_name = this.firstName,
        last_name = this.lastName,
        profile_picture = this.profilePicture,
        password = if(isPasswordChange) this.newPassword.sha256() else this.oldPassword.sha256()
    )
}
