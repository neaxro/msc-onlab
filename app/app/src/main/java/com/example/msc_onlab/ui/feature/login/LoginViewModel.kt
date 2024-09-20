package com.example.msc_onlab.ui.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.msc_onlab.data.model.login.Data
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.repository.LoginRepository
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.validateUserPassword
import com.example.msc_onlab.helpers.validateUserUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _loginData = MutableStateFlow<LoginData>(LoginData(username = "axel", password = "Asdasd11"))
    val loginData = _loginData.asStateFlow()

    private val _errors = MutableStateFlow<LoginFieldErrors>(LoginFieldErrors())
    val errors = _errors.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.NotLoggedIn)
    val loginState = _loginState.asStateFlow()

    private fun login(){
        _loginData.update {
            it.copy(
                username = "",
                password = ""
            )
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
                        userName = validateUserUsername(action.username.trim(), applicationContext)
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
