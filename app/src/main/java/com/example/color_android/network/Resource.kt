package com.example.color_android.network

import java.lang.Exception

sealed class Response<out T>{
    data class Success<out T>(val value: T) : Response<T>()
    data class Failure(val exception: String) : Response<Nothing>()
}
