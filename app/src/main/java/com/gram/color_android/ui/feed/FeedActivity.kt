package com.gram.color_android.ui.feed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gram.color_android.R
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.angry.FeedFragment
import com.gram.color_android.ui.profile.ProfileFragment
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.OnBackPressedListener
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private var listener : OnBackPressedListener? = null

    companion object{
        private var feel : FeelSet? = null
        fun getFeel() : FeelSet {
            if(feel == null)
                feel = FeelSet.ANGRY
            return feel!!
        }
        fun setFeel(feel : FeelSet){
            this.feel = feel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.feed_fragment_container, FeedFragment()).commit()

        feed_write_ib.setOnClickListener{
            val intent = Intent(this@FeedActivity, WriteActivity::class.java)
            startActivity(intent)
        }

        feed_my_page_ib.setOnClickListener{
            replaceFragment(ProfileFragment())
        }

        val feels = arrayListOf(FeelSet.LOVE, FeelSet.SHAME, FeelSet.BORED, FeelSet.SAD, FeelSet.HAPPY, FeelSet.ANGRY)
        circle_menu.setOnItemClickListener{
                buttonIndex -> feel = feels[buttonIndex]
            replaceFragment(FeedFragment())
        }
    }

    override fun onBackPressed() {
        if(listener != null)
            listener!!.onBackPressed()
        else
            super.onBackPressed()
    }

    fun setOnBackPressedListener(listener: OnBackPressedListener){
        this.listener = listener
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.feed_fragment_container, fragment).commit()
    }
}