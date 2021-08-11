package com.gram.color_android.ui.write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.gram.color_android.R
import com.gram.color_android.databinding.ActivityWriteBinding
import com.gram.color_android.network.set.FeelSet
import kotlinx.android.synthetic.main.activity_write.*
import org.w3c.dom.Text

class WriteActivity : AppCompatActivity() {


    private var feel : FeelSet? = null
    private var curView : TextView? = null
    private lateinit var dataBinding : ActivityWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        dataBinding.lifecycleOwner = this
        dataBinding.activity = this
        
    }

    fun selectFeel(tv : TextView, feel: FeelSet){
        if(curView == null) {
            curView = tv
            curView!!.visibility = View.VISIBLE
        }
        else {
            curView!!.visibility = View.GONE
            curView = tv
            curView!!.visibility = View.VISIBLE
        }
        this.feel = feel
    }
}