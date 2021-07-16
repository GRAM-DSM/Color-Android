package com.example.color_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.color_android.view.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragment = LoginFragment()

        val transaction= fragmentManager.beginTransaction()
        transaction.replace(R.id.main_Frame, fragment).commitAllowingStateLoss()
    }
}