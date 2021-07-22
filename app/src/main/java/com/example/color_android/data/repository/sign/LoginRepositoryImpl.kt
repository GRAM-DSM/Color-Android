package com.example.color_android.data.repository.sign

import com.example.color_android.data.model.sign.LoginRequest
import com.example.color_android.data.model.sign.LoginResponse
import com.example.color_android.data.repository.BaseRepository
import com.example.color_android.network.RetrofitClient
import com.example.color_android.util.SharedPreferencesHelper

class LoginRepositoryImpl : LoginRepository {
    override suspend fun login(body: LoginRequest) {
        RetrofitClient.getAPI().login(body)
    }


    override fun setToken(tokenResponse: LoginResponse){
        SharedPreferencesHelper.getInstance().access_token = "Bearer " + tokenResponse.access_token
        SharedPreferencesHelper.getInstance().refresh_token = "Bearer " + tokenResponse.refresh_token
    }
}