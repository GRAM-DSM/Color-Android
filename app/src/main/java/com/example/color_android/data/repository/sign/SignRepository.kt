package com.example.color_android.data.repository.sign

import com.example.color_android.data.model.sign.LoginRequest
import com.example.color_android.data.model.sign.LoginResponse
import com.example.color_android.data.model.sign.RegisterRequest
import retrofit2.Response

interface SignRepository {
    suspend fun login(body: LoginRequest) : Response<LoginResponse>

    fun setToken(tokenResponse: LoginResponse)

    suspend fun register(body: RegisterRequest) : Response<Void>

    suspend fun nameCheck(nickname: HashMap<String, String>) : Response<Void>

    suspend fun sendEmail(body: HashMap<String, String>) : Response<Void>

    suspend fun emailCertify(email : String, code : String) : Response<Void>
}