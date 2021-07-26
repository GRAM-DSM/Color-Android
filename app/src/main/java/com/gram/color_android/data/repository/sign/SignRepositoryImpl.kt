package com.gram.color_android.data.repository.sign

import com.gram.color_android.data.model.sign.LoginRequest
import com.gram.color_android.data.model.sign.LoginResponse
import com.gram.color_android.data.model.sign.RegisterRequest
import com.gram.color_android.network.RetrofitClient
import com.gram.color_android.network.SafeApiRequest
import com.gram.color_android.util.SharedPreferencesHelper
import retrofit2.Response

class SignRepositoryImpl : SignRepository, SafeApiRequest() {
    override suspend fun login(body: LoginRequest) : Response<LoginResponse> {
        return safeApiCall { RetrofitClient.getAPI().login(body) }
    }

    override fun setToken(tokenResponse: LoginResponse) {
        SharedPreferencesHelper.getInstance().access_token = "Bearer " + tokenResponse.access_token
        SharedPreferencesHelper.getInstance().refresh_token =
            "Bearer " + tokenResponse.refresh_token
    }

    override suspend fun register(body: RegisterRequest): Response<Void> {
        return safeApiCall { RetrofitClient.getAPI().register(body) }
    }

    override suspend fun nameCheck(nickname: HashMap<String, String>): Response<Void> {
        return safeApiCall { RetrofitClient.getAPI().nameCheck(nickname) }
    }

    override suspend fun sendEmail(body: HashMap<String, String>): Response<Void> {
        return safeApiCall { RetrofitClient.getAPI().sendEmail(body) }
    }

    override suspend fun emailCertify(
        email: String,
        code: String
    ): Response<Void> {
        return safeApiCall { RetrofitClient.getAPI().emailCertify(email, code) }
    }
}