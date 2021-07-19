package com.example.color_android.data.repository

import com.example.color_android.data.EmailCertifyRequest
import com.example.color_android.data.RegisterRequest
import com.example.color_android.network.RetrofitClient

class RegisterRepository : BaseRepository(){
    suspend fun register(body: RegisterRequest) = safeApiCall{
        RetrofitClient.getAPI().register(body)
    }

    suspend fun nicknameCheck(body: HashMap<String, String>) = safeApiCall {
        RetrofitClient.getAPI().nicknameCheck(body)
    }

    suspend fun sendEmail(body: HashMap<String, String>) = safeApiCall {
        RetrofitClient.getAPI().sendEmail(body)
    }

    suspend fun emailCertify(body: EmailCertifyRequest) = safeApiCall {
        RetrofitClient.getAPI().emailCertify(body)
    }
}