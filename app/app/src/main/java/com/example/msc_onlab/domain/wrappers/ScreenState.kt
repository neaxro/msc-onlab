package com.example.msc_onlab.domain.wrappers

import android.content.Context
import com.example.msc_onlab.MyApp
import com.example.msc_onlab.R

sealed class ScreenState(
    val message: String = "",
    val show: Boolean,
) {
    class Loading(val context: Context = MyApp.applicationContext(), message: String = "Loading...", show: Boolean = false) : ScreenState(message, show)
    class Finished(val context: Context = MyApp.applicationContext(), message: String = "Finished!", show: Boolean = false) : ScreenState(message, show)
    class Error(val context: Context = MyApp.applicationContext(), message: String, show: Boolean = true) : ScreenState(message, show)
    class Success(val context: Context = MyApp.applicationContext(), message: String = "Success!", show: Boolean = false) : ScreenState(message, show)
}