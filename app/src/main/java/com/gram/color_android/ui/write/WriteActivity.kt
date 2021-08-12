package com.gram.color_android.ui.write

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

        var hashTag : MutableList<String> = mutableListOf()
        write_post_btn.setOnClickListener{
            println(write_tag_et.text.toString().split("#"))
        }
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

    fun post(access_token: String, body: WriteRequest){
        if(nullCheck())
            writeViewModel.write(access_token, body)
    }

    private fun nullCheck() = (feel != null && !write_content_et.text.toString().equals(""))
}