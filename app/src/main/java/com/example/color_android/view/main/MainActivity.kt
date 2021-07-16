package com.example.color_android.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.core.content.res.ResourcesCompat
import com.example.color_android.R
import com.example.color_android.view.sign.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragment = LoginFragment()

        val transaction= fragmentManager.beginTransaction()
//        transaction.replace(R.id.main_Frame, fragment).commitAllowingStateLoss()
        val colorText = main_explain_tv.text
        val content1 = SpannableString(colorText)

        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.angry, null)), 11, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.love, null)), 12, 13, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.happy, null)), 13, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.sad, null)), 14, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.bored, null)), 15, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        main_explain_tv.text = content1
    }
}