package com.example.color_android.data.repository

import com.example.color_android.data.LoginRequest
import com.example.color_android.data.LoginResponse

interface LoginRepository {
    suspend fun login(body: LoginRequest): LoginResponse


}