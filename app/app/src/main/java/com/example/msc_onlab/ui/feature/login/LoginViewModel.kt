package com.example.msc_onlab.ui.feature.login

import androidx.lifecycle.ViewModel
import com.example.msc_onlab.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

}