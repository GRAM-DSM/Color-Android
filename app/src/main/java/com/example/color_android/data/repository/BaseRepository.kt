package com.example.color_android.data.repository

import com.example.color_android.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Resource<T?> {
        return try {
            if (apiCall.invoke().isSuccessful) {
                Resource.Success(apiCall.invoke().body(), apiCall.invoke().code())
            } else {
                Resource.Failure(apiCall.invoke().message())
            }
        } catch (e: Exception) {
            Resource.Failure(e.message.toString())
        }
    }
}