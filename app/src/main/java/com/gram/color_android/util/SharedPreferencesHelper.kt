package com.gram.color_android.util

import android.content.Context

class SharedPreferencesHelper {
    private val prefs = ColorApplication.context!!.getSharedPreferences(
        "Color-Android", Context.MODE_PRIVATE
    )

    var accessToken: String?
        get() = prefs.getString("access_token", null)
        set(value){
            val editor = prefs.edit()
            editor.putString("access_token", value)
            editor.apply()
        }

    var refreshToken: String?
        get() = prefs.getString("refresh_token", null)
        set(value){
            val editor = prefs.edit()
            editor.putString("refresh_token", value)
            editor.apply()
        }

    var isLogin: Boolean
        get() = prefs.getBoolean("isLogin", false)
        set(value){
            val editor = prefs.edit()
            editor.putBoolean("isLogin", value)
            editor.apply()
        }

    var email: String?
        get() = prefs.getString("email", null)
        set(value){
            val editor = prefs.edit()
            editor.putString("email", value)
            editor.apply()
        }

    companion object {
        private var sharedPreferencesHelper: SharedPreferencesHelper? = null

        fun getInstance(): SharedPreferencesHelper {
            if (sharedPreferencesHelper == null)
                sharedPreferencesHelper = SharedPreferencesHelper()
            return sharedPreferencesHelper!!
        }
    }
}