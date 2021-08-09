package com.gram.color_android.ui.feed.angry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.ui.sign.LoginFragment
import com.gram.color_android.util.BottomSheetFragment
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.feed_fragment_container, FeedAngryFragment()).commit()

        val bottomSheetDialog = BottomSheetFragment()
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
    }
}