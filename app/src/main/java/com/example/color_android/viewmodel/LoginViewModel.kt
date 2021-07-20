package com.example.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.color_android.data.LoginRequest
import com.example.color_android.data.LoginResponse
import com.example.color_android.data.repository.LoginRepository
import com.example.color_android.network.Resource
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()
    private val _loginLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loginLiveData: MutableLiveData<Boolean> = _loginLiveData

    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            when(val resource = loginRepository.login(loginRequest)){
                is Resource.Success -> {
                    successLogin(resource)
                }
            }
        }
    }

    private fun successLogin(resource: Resource.Success<LoginResponse>) {
        if(resource.status == 200){
            _loginLiveData.postValue(true)
            loginRepository.setToken(resource.value)
        }
    }
}