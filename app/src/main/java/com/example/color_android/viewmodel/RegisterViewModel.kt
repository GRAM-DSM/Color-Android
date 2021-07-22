package com.example.color_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.color_android.data.model.sign.RegisterRequest
import com.example.color_android.data.repository.sign.SignRepositoryImpl
import com.example.color_android.network.RegisterSet
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val signRepository = SignRepositoryImpl()
    private val _registerLiveData : MutableLiveData<RegisterSet> = MutableLiveData()
    val registerLiveData : LiveData<RegisterSet> = _registerLiveData

//    fun register(body: RegisterRequest){
//        viewModelScope.launch {
//            val response = signRepository.register(body)
//            if(response.isSuccessful)
//        }
//    }

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

    private fun nameCheckSuccess(response: Response<Void>){
        if(response.code() == 200){
            _registerLiveData.postValue(RegisterSet.NameSuccess)
        } else {
            _registerLiveData.postValue(RegisterSet.NameFail)
        }
    }
}