package com.gram.color_android.util

import android.app.Application
import android.content.Context

class ColorApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        lateinit var context: Context
            private set
    }
}