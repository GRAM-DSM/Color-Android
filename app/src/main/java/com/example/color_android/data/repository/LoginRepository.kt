package com.example.color_android.data.repository

import com.example.color_android.data.LoginRequest
import com.example.color_android.network.RetrofitClient

class LoginRepository : BaseRepository() {
    suspend fun login(body: LoginRequest) = safeApiCall {
        RetrofitClient.getAPI().login(body)
    }
}