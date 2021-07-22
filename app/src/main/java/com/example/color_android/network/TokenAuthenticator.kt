package com.example.color_android.network

import android.content.Intent
import android.util.Log
import com.example.color_android.util.ColorApplication
import com.example.color_android.util.SharedPreferencesHelper
import com.example.color_android.ui.sign.SignActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response

class TokenAuthenticator : Interceptor {
    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance()
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        when (response.code) {
            401 -> {
                if (!sharedPreferencesHelper.isLogout) {
                    if (sharedPreferencesHelper.access_token == null) {
                        val intent = Intent(ColorApplication.context, SignActivity::class.java)
                        intent.apply {
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        ColorApplication.context.startActivity(intent)
                        sharedPreferencesHelper.isLogout = true
                    }
                    else {
                        sharedPreferencesHelper.access_token = null

                        val refreshToken = sharedPreferencesHelper.refresh_token
                        CoroutineScope(Dispatchers.IO).launch {
                            if(refreshToken != null)
                                getAccessToken(refreshToken)
                        }
                    }
                }
            }
        }
        return response
    }

    private suspend fun getAccessToken(refresh_token: String) {
        val accessToken = withContext(Dispatchers.IO) {
            RetrofitClient.getAPI().tokenRefresh(refresh_token)
        }

        if (accessToken.isSuccessful) {
            if (accessToken.code() == 204)
                sharedPreferencesHelper.access_token = "Bearer " + accessToken.body()
        } else
            Log.e("TokenAuthenticator", accessToken.message())
    }
}