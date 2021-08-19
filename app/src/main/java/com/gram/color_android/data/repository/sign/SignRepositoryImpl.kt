package com.gram.color_android.data.repository.sign

import com.gram.color_android.data.model.sign.LoginRequest
import com.gram.color_android.data.model.sign.LoginResponse
import com.gram.color_android.data.model.sign.RegisterRequest
import com.gram.color_android.network.RetrofitClient
import com.gram.color_android.network.SafeApiRequest
import com.gram.color_android.util.SharedPreferencesHelper
import retrofit2.Response

class SignRepositoryImpl : SignRepository, SafeApiRequest() {

    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance()

    override suspend fun login(body: LoginRequest): Response<LoginResponse> {
        return safeApiCall { RetrofitClient.getSpringAPI().login(body) }
    }

    override fun setToken(tokenResponse: LoginResponse) {
        sharedPreferencesHelper.accessToken = "Bearer " + tokenResponse.access_token
        sharedPreferencesHelper.refreshToken =
            "Bearer " + tokenResponse.refresh_token
        sharedPreferencesHelper.isLogin = true
    }

    override suspend fun register(body: RegisterRequest): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().register(body) }
    }

    override suspend fun nameCheck(nickname: HashMap<String, String>): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().nameCheck(nickname) }
    }

    override suspend fun sendEmail(body: HashMap<String, String>): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().sendEmail(body) }
    }

    override suspend fun emailCertify(
        email: String,
        code: String
    ): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().emailCertify(email, code) }
    }
}