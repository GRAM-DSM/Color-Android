package com.example.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.color_android.data.model.sign.LoginRequest
import com.example.color_android.data.model.sign.LoginResponse
import com.example.color_android.data.repository.sign.SignRepositoryImpl
import com.example.color_android.network.set.LoginSet
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val signRepository = SignRepositoryImpl()
    private val _loginLiveData : MutableLiveData<LoginSet> = MutableLiveData()
    val loginLiveData : MutableLiveData<LoginSet> = _loginLiveData

    fun login(body : LoginRequest){
        viewModelScope.launch {
            val response = signRepository.login(body)
            if(response.isSuccessful){
                loginSuccess(response)
            } else {
                _loginLiveData.postValue(LoginSet.LOGIN_FAIL)
            }
        }
    }

    private fun loginSuccess(response: Response<LoginResponse>){
        if(response.code() == 200){
            _loginLiveData.postValue(LoginSet.LOGIN_SUCCESS)
            signRepository.setToken(response.body()!!)
        } else {
            _loginLiveData.postValue(LoginSet.LOGIN_FAIL)
        }
    }
}