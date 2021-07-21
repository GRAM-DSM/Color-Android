package com.example.color_android.data.repository

import com.example.color_android.data.model.sign.LoginRequest
import com.example.color_android.data.model.sign.LoginResponse
import com.example.color_android.network.RetrofitClient
import com.example.color_android.util.SharedPreferencesHelper

class LoginRepository : BaseRepository() {
    suspend fun login(body: LoginRequest) = safeApiCall {
        RetrofitClient.getAPI().login(body)
    }

    fun setToken(tokenResponse: LoginResponse){
        SharedPreferencesHelper.getInstance().access_token = "Bearer " + tokenResponse.access_token
        SharedPreferencesHelper.getInstance().refresh_token = "Bearer " + tokenResponse.refresh_token
    }
}