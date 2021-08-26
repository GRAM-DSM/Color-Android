package com.gram.color_android.ui.feed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.gram.color_android.R
import com.gram.color_android.databinding.ActivityFeedBinding
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.angry.FeedFragment
import com.gram.color_android.ui.profile.ProfileFragment
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.OnBackPressedListener
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private var listener : OnBackPressedListener? = null
    private lateinit var dataBinding : ActivityFeedBinding
    val liveData : MutableLiveData<Int> = MutableLiveData()

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
        dataBinding = DataBindingUtil.setContentView(this@FeedActivity, R.layout.activity_feed)
        dataBinding.activity = this@FeedActivity

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
                liveData.postValue(getFeelColor(feel!!))

            replaceFragment(FeedFragment())
        }
    }

    private fun getFeelColor(feel: FeelSet) : Int{
        val colors = arrayListOf(R.color.angry, R.color.happy, R.color.sad, R.color.bored, R.color.shame, R.color.love)
        var position = when(feel){
            FeelSet.ANGRY -> 0
            FeelSet.HAPPY -> 1
            FeelSet.SAD -> 2
            FeelSet.BORED -> 3
            FeelSet.SHAME -> 4
            FeelSet.LOVE -> 5
        }
        return colors[position]
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