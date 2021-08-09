package com.gram.color_android.ui.feed.angry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gram.color_android.R

class FeedActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.feed_fragment_container, FeedAngryFragment()).commit()
    }


}