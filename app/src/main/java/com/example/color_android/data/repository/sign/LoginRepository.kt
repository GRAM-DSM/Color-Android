package com.example.color_android.data.repository.sign

import com.example.color_android.data.model.sign.LoginRequest
import com.example.color_android.data.model.sign.LoginResponse

interface LoginRepository {
    suspend fun login(body: LoginRequest)

    fun setToken(tokenResponse: LoginResponse)
}