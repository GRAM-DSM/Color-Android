package com.example.color_android.data.repository.sign

import com.example.color_android.data.model.sign.EmailCertifyRequest
import com.example.color_android.data.model.sign.RegisterRequest
import com.example.color_android.data.repository.BaseRepository
import com.example.color_android.network.RetrofitClient
import com.example.color_android.network.SafeApiRequest

class RegisterRepositoryImpl : SafeApiRequest(), RegisterRepository {
    override suspend fun register(body: RegisterRequest) {
        safeApiCall { RetrofitClient.getAPI().register(body) }
    }

    override suspend fun nicknameCheck(body: HashMap<String, String>) {
        safeApiCall { RetrofitClient.getAPI().nicknameCheck(body) }
    }

    override suspend fun sendEmail(body: HashMap<String, String>) {
        safeApiCall { RetrofitClient.getAPI().sendEmail(body) }
    }

    override suspend fun emailCertify(body: EmailCertifyRequest) {
        safeApiCall { RetrofitClient.getAPI().emailCertify(body) }
    }
}