package com.example.msc_onlab.ui.feature.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.repository.register.RegisterRepository
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.sha256
import com.example.msc_onlab.helpers.validateFirstname
import com.example.msc_onlab.helpers.validateLastname
import com.example.msc_onlab.helpers.validatePasswordsMatch
import com.example.msc_onlab.helpers.validateUserEmail
import com.example.msc_onlab.helpers.validateUserPassword
import com.example.msc_onlab.helpers.validateUsername
import com.example.msc_onlab.data.model.register.RegisterData
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.helpers.or
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _registerData = MutableStateFlow<RegisterData>(RegisterData())
    val registerData = _registerData.asStateFlow()

    private val _rePassword = MutableStateFlow<String>("")
    val rePassword = _rePassword.asStateFlow()

    private val _errors = MutableStateFlow<RegisterFieldErrors>(RegisterFieldErrors())
    val errors = _errors.asStateFlow()

    private fun register(){
        checkAllValues()
        if(_errors.value.anyErrors()) return

        _screenState.value = ScreenState.Loading()

        val hashedRegisterData = _registerData.value.copy(
            password = _registerData.value.password.sha256()
        )

        viewModelScope.launch(Dispatchers.IO) {
            var result = registerRepository.register(registerData = hashedRegisterData)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success(message = result.message!!, show = true)
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: RegisterAction){
        when(action){
            RegisterAction.Register -> {
                register()
            }
            is RegisterAction.UpdateEmail -> {
                _registerData.update {
                    it.copy(
                        email = action.email.trim()
                    )
                }

                _errors.update {
                    it.copy(
                        email = validateUserEmail(action.email.trim(), applicationContext)
                    )
                }
            }
            is RegisterAction.UpdateFirstname -> {
                _registerData.update {
                    it.copy(
                        first_name = action.firstname.trim()
                    )
                }

                _errors.update {
                    it.copy(
                        firstname = validateFirstname(action.firstname.trim(), applicationContext)
                    )
                }
            }
            is RegisterAction.UpdateLastname -> {
                _registerData.update {
                    it.copy(
                        last_name = action.lastname.trim()
                    )
                }

                _errors.update {
                    it.copy(
                        lastname = validateLastname(action.lastname.trim(), applicationContext)
                    )
                }
            }
            is RegisterAction.UpdatePassword -> {
                _registerData.update {
                    it.copy(
                        password = action.password.trim()
                    )
                }

                _errors.update {
                    it.copy(
                        password = validateUserPassword(action.password.trim(), applicationContext)
                    )
                }
            }
            is RegisterAction.UpdateProfilePicture -> TODO()
            is RegisterAction.UpdateRePassword -> {
                _rePassword.value = action.rePassword.trim()

                _errors.update {
                    val passwordCheckResult = validateUserPassword(action.rePassword.trim(), applicationContext)
                    it.copy(
                        rePassword = if(passwordCheckResult !is DataFieldErrors.NoError){
                            passwordCheckResult
                        }
                        else{
                            validatePasswordsMatch(
                                password = _registerData.value.password,
                                passwordChange = action.rePassword.trim(),
                                context = applicationContext
                            )
                        }
                    )
                }
            }
            is RegisterAction.UpdateUsername -> {
                _registerData.update {
                    it.copy(
                        username = action.username.trim()
                    )
                }

                _errors.update {
                    it.copy(
                        username = validateUsername(action.username.trim(), applicationContext)
                    )
                }
            }
        }
    }

    private fun checkAllValues(){
        val firstnameCheck = validateFirstname(_registerData.value.first_name, applicationContext)
        val lastnameCheck = validateLastname(_registerData.value.last_name, applicationContext)
        val usernameCheck = validateUsername(_registerData.value.username, applicationContext)
        val emailCheck = validateUserEmail(_registerData.value.email, applicationContext)
        val passwordCheck = validateUserPassword(_registerData.value.password, applicationContext)
        val passwordMachCheck = validatePasswordsMatch(_registerData.value.password, _rePassword.value, applicationContext)

        _errors.update {
            it.copy(
                firstname = firstnameCheck,
                lastname = lastnameCheck,
                username = usernameCheck,
                email = emailCheck,
                password = passwordCheck,
                rePassword = passwordMachCheck
            )
        }
    }
}

sealed class RegisterAction{
    data object Register : RegisterAction()
    class UpdateFirstname(val firstname: String) : RegisterAction()
    class UpdateLastname(val lastname: String) : RegisterAction()
    class UpdateUsername(val username: String) : RegisterAction()
    class UpdateEmail(val email: String) : RegisterAction()
    class UpdateProfilePicture(val profilePicture: String) : RegisterAction()
    class UpdatePassword(val password: String) : RegisterAction()
    class UpdateRePassword(val rePassword: String) : RegisterAction()
}

data class RegisterFieldErrors(
    val firstname: DataFieldErrors = DataFieldErrors.NoError,
    val lastname: DataFieldErrors = DataFieldErrors.NoError,
    val username: DataFieldErrors = DataFieldErrors.NoError,
    val email: DataFieldErrors = DataFieldErrors.NoError,
    val profilePicture: DataFieldErrors = DataFieldErrors.NoError,
    val password: DataFieldErrors = DataFieldErrors.NoError,
    val rePassword: DataFieldErrors = DataFieldErrors.NoError,
)

fun RegisterFieldErrors.anyErrors(): Boolean {
    val error = this.firstname
        .or(this.lastname)
        .or(this.username)
        .or(this.email)
        .or(this.password)

    return error !is DataFieldErrors.NoError
}