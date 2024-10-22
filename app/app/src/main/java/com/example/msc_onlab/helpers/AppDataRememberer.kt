package com.example.msc_onlab.helpers

import android.content.SharedPreferences
import com.example.msc_onlab.data.model.login.LoginData

object AppDataRememberer {
    private const val usernameKey = "username"
    private const val passwordKey = "password"

    fun rememberLoginData(sharedPreferences: SharedPreferences, username: String, password: String){
        val editor = sharedPreferences.edit()
        with(editor){
            putString(usernameKey, username)
            putString(passwordKey, password)
            apply()
        }
    }

    fun getLoginData(sharedPreferences: SharedPreferences): LoginData? {
        val username = sharedPreferences.getString(usernameKey, "")
        val password = sharedPreferences.getString(passwordKey, "")

        return if(username?.isNotEmpty() == true && password?.isNotEmpty() == true){
            LoginData(username = username, password = password)
        }
        else{
            null
        }
    }

    fun forgetLoginData(sharedPreferences: SharedPreferences){
        rememberLoginData(sharedPreferences = sharedPreferences, username = "", password = "")
    }
}