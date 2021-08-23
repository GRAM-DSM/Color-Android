package com.gram.color_android.ui.write

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.gram.color_android.R
import com.gram.color_android.data.model.write.WriteRequest
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

        val intent = intent
        if (intent.hasExtra("content")) {
            write_content_et.setText(intent.getStringExtra("content"))
            write_post_btn.setOnClickListener{
                if(nullCheck()){
                    val body = WriteRequest(write_content_et.text.toString(), feel.toString(), getTag())
                    updatePost(prefs.accessToken!!, intent.getStringExtra("post_id")!!, body)
                }
            }
        } else {
            write_post_btn.setOnClickListener {
                if (nullCheck()) {
                    val body = WriteRequest(write_content_et.text.toString(), feel.toString(), getTag())
                    post(prefs.accessToken!!, body)
                }
            }
        }

        writeViewModel.writeLiveData.observe(this, {
            when (it) {
                WriteSet.WRITE_SUCCESS, WriteSet.UPDATE_SUCCESS -> {
                    val intent = Intent(this@WriteActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                WriteSet.WRITE_FAIL, WriteSet.UPDATE_FAIL -> Toast.makeText(
                    this@WriteActivity,
                    getString(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()
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
        when (feel) {
            FeelSet.ANGRY -> write_post_btn.setBackgroundColor(getColor(R.color.angry))
            FeelSet.HAPPY -> write_post_btn.setBackgroundColor(getColor(R.color.happy))
            FeelSet.SAD -> write_post_btn.setBackgroundColor(getColor(R.color.sad))
            FeelSet.BORED -> write_post_btn.setBackgroundColor(getColor(R.color.bored))
            FeelSet.LOVE -> write_post_btn.setBackgroundColor(getColor(R.color.love))
            FeelSet.SHAME -> write_post_btn.setBackgroundColor(getColor(R.color.shame))
        }
    }

    private fun post(access_token: String, body: WriteRequest) {
        writeViewModel.write(access_token, body)
    }

    private fun updatePost(access_token: String, post_id: String, body: WriteRequest){
        writeViewModel.updatePost(access_token, post_id, body)
    }

    private fun nullCheck() = (feel != null && !write_content_et.text.toString().equals(""))

    private fun getTag() : MutableList<String> {
        val hashTag: MutableList<String> = write_tag_et.text.toString().split("#") as MutableList<String>
        if (write_tag_et.text.toString() != "") {
            hashTag.removeAt(0)
        }
        return hashTag
    }
}