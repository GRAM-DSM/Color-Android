package com.gram.color_android.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.gram.color_android.R
import com.gram.color_android.ui.angry.FeedActivity
import com.gram.color_android.ui.explain.ExplainActivity
import com.gram.color_android.util.Color
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorText = main_explain_tv.text
        val content1 = SpannableString(colorText)

        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.angry, null)), 11, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.love, null)), 12, 13, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.happy, null)), 13, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.sad, null)), 14, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.bored, null)), 15, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        main_explain_tv.text = content1

        main_explain_ib.setOnClickListener{
            val intent = Intent(this@MainActivity, ExplainActivity::class.java)
            startActivity(intent)
        }
        main_angry_tv.setOnClickListener{
            feedIntent(this@MainActivity, FeedActivity::class.java, Color.ANGRY)
        }
    }

    private fun <T> feedIntent(packageContext: Context, cls: Class<T>, color: Color){
        val intent = Intent(packageContext, cls)
        intent.putExtra("color", color)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}