package com.example.msc_onlab

import androidx.lifecycle.ViewModel
import com.example.msc_onlab.domain.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
): ViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private var number = 0

    init {
        _name.value = "Axel"
    }

    fun updateName(){
        _name.value = "Axel ${number++}"
    }
}