package com.gram.color_android.ui.write

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.gram.color_android.R
import com.gram.color_android.data.model.WriteRequest
import com.gram.color_android.databinding.ActivityWriteBinding
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.network.set.WriteSet
import com.gram.color_android.ui.feed.FeedActivity
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.WriteViewModel
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityWriteBinding
    private val writeViewModel = WriteViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()
    private var feel: FeelSet? = null
    private var curView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        dataBinding.lifecycleOwner = this
        dataBinding.activity = this

        write_post_btn.setOnClickListener{
            if(nullCheck()){
                val hashTag : MutableList<String> = write_tag_et.text.toString().split("#") as MutableList<String>
                hashTag.removeAt(0)
                val body = WriteRequest(write_content_et.text.toString(), feel.toString(), hashTag)
                post(prefs.access_token!!, body)
            }
        }

        writeViewModel.writeLiveData.observe(this, {
            when(it){
                WriteSet.WRITE_SUCCESS -> {
                    val intent = Intent(this@WriteActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                WriteSet.WRITE_FAIL -> Toast.makeText(this@WriteActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun selectFeel(tv: TextView, feel: FeelSet) {
        if (curView == null) {
            curView = tv
            curView!!.visibility = View.VISIBLE
        } else {
            curView!!.visibility = View.GONE
            curView = tv
            curView!!.visibility = View.VISIBLE
        }
        this.feel = feel
        when(feel){
            FeelSet.ANGRY -> write_post_btn.setBackgroundColor(getColor(R.color.angry))
            FeelSet.HAPPY -> write_post_btn.setBackgroundColor(getColor(R.color.happy))
            FeelSet.SAD -> write_post_btn.setBackgroundColor(getColor(R.color.sad))
            FeelSet.BORED -> write_post_btn.setBackgroundColor(getColor(R.color.bored))
            FeelSet.LOVE -> write_post_btn.setBackgroundColor(getColor(R.color.love))
            FeelSet.SHAME -> write_post_btn.setBackgroundColor(getColor(R.color.shame))
        }
    }

    private fun post(access_token: String, body: WriteRequest){
            writeViewModel.write(access_token, body)
    }

    private fun nullCheck() = (feel != null && !write_content_et.text.toString().equals(""))
}