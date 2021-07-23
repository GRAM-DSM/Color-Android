package com.example.color_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.color_android.data.model.sign.EmailCertifyRequest
import com.example.color_android.data.model.sign.RegisterRequest
import com.example.color_android.data.repository.sign.SignRepositoryImpl
import com.example.color_android.network.RegisterSet
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val signRepository = SignRepositoryImpl()
    private val _registerLiveData : MutableLiveData<RegisterSet> = MutableLiveData()
    val registerLiveData : LiveData<RegisterSet> = _registerLiveData

    fun nameCheck(nickname : String){
        val body = HashMap<String, String>()
        body["nickname"] = nickname

        viewModelScope.launch {
            val response = signRepository.nameCheck(body)
            if(response.isSuccessful){
                nameCheckSuccess(response)
            } else {
                _registerLiveData.postValue(RegisterSet.NameFail)
            }
        }
    }

    fun sendEmail(email : String){
        val body = HashMap<String, String>()
        body["email"] = email
        viewModelScope.launch {
            val response = signRepository.sendEmail(body)
            if(response.isSuccessful){
                sendEmailSuccess(response)
            } else{
                _registerLiveData.postValue(RegisterSet.SendEmailFail)
            }
        }
    }

    fun emailCertify(emailCertifyRequest: EmailCertifyRequest){
        viewModelScope.launch {
            val response = signRepository.emailCertify(emailCertifyRequest)
            if(response.isSuccessful){
                emailCertifySuccess(response)
            } else {
                _registerLiveData.postValue(RegisterSet.EmailFail)
            }
        }
    }

    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            val response = signRepository.register(registerRequest)
            if(response.isSuccessful){
                registerSuccess(response)
            } else {
                _registerLiveData.postValue(RegisterSet.RegisterFail)
            }
        }
    }

    private fun nameCheckSuccess(response: Response<Void>){
        if(response.code() == 200){
            _registerLiveData.postValue(RegisterSet.NameSuccess)
        } else {
            _registerLiveData.postValue(RegisterSet.NameFail)
        }
    }

    private fun sendEmailSuccess(response: Response<Void>){
        if(response.code() == 200){
            _registerLiveData.postValue(RegisterSet.SendEmailSuccess)
        } else {
            _registerLiveData.postValue(RegisterSet.SendEmailFail)
        }
    }

    private fun emailCertifySuccess(response: Response<Void>){
        if(response.code() == 200){
            _registerLiveData.postValue(RegisterSet.EmailSuccess)
        } else {
            _registerLiveData.postValue(RegisterSet.EmailFail)
        }
    }

    private fun registerSuccess(response: Response<Void>){
        if(response.code() == 201){
            _registerLiveData.postValue(RegisterSet.RegisterSuccess)
        } else {
            _registerLiveData.postValue(RegisterSet.RegisterFail)
        }
    }
}