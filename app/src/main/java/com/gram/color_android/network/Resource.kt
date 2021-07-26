package com.gram.color_android.network

sealed class Resource<out T>{
    data class Success<out T>(val value: T, val status: Int) : Resource<T?>()
    data class Failure(val errorBody: String?) : Resource<Nothing>()
}
