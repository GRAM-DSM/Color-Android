package com.example.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.color_android.data.LoginRequest
import com.example.color_android.data.LoginResponse
import com.example.color_android.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginLiveData: MutableLiveData<LoginResponse> = MutableLiveData()

    fun login(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getAPI().login(LoginRequest("1", "2"))
        }
    }
}