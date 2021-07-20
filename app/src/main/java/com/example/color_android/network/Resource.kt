package com.example.color_android.network

import okhttp3.ResponseBody

sealed class Resource<out T>{
    data class Success<out T>(val value: T, val status: Int) : Resource<T?>()
    data class Failure(val errorBody: String?) : Resource<Nothing>()
}