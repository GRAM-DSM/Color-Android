package com.example.color_android.data.repository.sign

import com.example.color_android.data.model.sign.EmailCertifyRequest
import com.example.color_android.data.model.sign.RegisterRequest

interface RegisterRepository {
    suspend fun register(body: RegisterRequest)

    suspend fun nicknameCheck(body: HashMap<String, String>)

    suspend fun sendEmail(body: HashMap<String, String>)

    suspend fun emailCertify(body: EmailCertifyRequest)
}