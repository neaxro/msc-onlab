package com.example.msc_onlab.ui.feature.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.data.repository.login.LoginRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.AppDataRememberer
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.helpers.sha256
import com.example.msc_onlab.helpers.validateUserPassword
import com.example.msc_onlab.helpers.validateUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sharedPreferences: SharedPreferences,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _loginData = MutableStateFlow<LoginData>(LoginData(username = "", password = ""))
    val loginData = _loginData.asStateFlow()

    private val _errors = MutableStateFlow<LoginFieldErrors>(LoginFieldErrors())
    val errors = _errors.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.NotLoggedIn)
    val loginState = _loginState.asStateFlow()

    init {
        val rememberedLoginData = AppDataRememberer.getLoginData(sharedPreferences)
        if(rememberedLoginData != null){
            _loginData.value = rememberedLoginData
            login()
        }
    }

    private fun login(){
        _screenState.value = ScreenState.Loading()

        val hashedLoginData = _loginData.value.copy(
            password = _loginData.value.password.sha256()
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = loginRepository.loginPerson(loginData = hashedLoginData)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    Log.i("LOGIN_STATUS", "Logged!")
                    Log.i("LOGIN_STATUS", result.data!!.toString())

                    AppDataRememberer.rememberLoginData(
                        sharedPreferences = sharedPreferences,
                        username = _loginData.value.username,
                        password = _loginData.value.password,
                    )

                    val loginResponse: LoginResponse = result.data!!
                    LoggedPersonData.TOKEN = loginResponse.data.token

                    _loginState.value = LoginState.LoggedIn
                }
                is Resource.Error -> {
                    Log.i("LOGIN_STATUS", "Error...")

                    _screenState.value = ScreenState.Error(message = result.message!!)
                    _loginState.value = LoginState.NotLoggedIn
                }
            }
        }
    }

    fun evoke(action: LoginAction){
        when(action){
            LoginAction.Login -> {
                login()
            }
            is LoginAction.UpdatePassword -> {
                _loginData.update {
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
            is LoginAction.UpdateUsername -> {
                _loginData.update {
                    it.copy(
                        username = action.username.trim()
                    )
                }

                _errors.update {
                    it.copy(
                        userName = validateUsername(action.username.trim(), applicationContext)
                    )
                }
            }
        }
    }
}

sealed class LoginAction{
    data object Login : LoginAction()
    class UpdateUsername(val username: String) : LoginAction()
    class UpdatePassword(val password: String) : LoginAction()
}

sealed class LoginState{
    data object LoggedIn : LoginState()
    data object NotLoggedIn : LoginState()
}

data class LoginFieldErrors(
    val userName: DataFieldErrors = DataFieldErrors.NoError,
    val password: DataFieldErrors = DataFieldErrors.NoError,
)
