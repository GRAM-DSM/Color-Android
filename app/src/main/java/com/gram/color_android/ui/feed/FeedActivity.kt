package com.gram.color_android.ui.feed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.ui.feed.angry.FeedAngryFragment
import com.gram.color_android.ui.sign.SignActivity
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.feed_fragment_container, FeedAngryFragment()).commit()

        feed_write_ib.setOnClickListener{
            val intent = Intent(this@FeedActivity, WriteActivity::class.java)
            startActivity(intent)
        }
    }
}