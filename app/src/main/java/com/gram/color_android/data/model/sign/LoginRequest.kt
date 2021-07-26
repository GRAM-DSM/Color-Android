package com.gram.color_android.data.model.sign

data class LoginRequest(
    val email: String,
    val password: String,
    val device_token: String)
