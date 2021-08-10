package com.gram.color_android.ui.write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.gram.color_android.R
import com.gram.color_android.network.set.FeelSet
import kotlinx.android.synthetic.main.activity_write.*
import org.w3c.dom.Text

class WriteActivity : AppCompatActivity() {


    private var feel : FeelSet? = null
    private var curView : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)


        write_angry_tv.setOnClickListener{
            selectFeel(angry_select_tv, FeelSet.ANGRY)
        }
        write_happy_tv.setOnClickListener{
            selectFeel(happy_select_tv, FeelSet.HAPPY)
        }
        write_sad_tv.setOnClickListener{
            selectFeel(sad_select_tv, FeelSet.SAD)
        }
        write_bored_tv.setOnClickListener{
            selectFeel(bored_select_tv, FeelSet.BORED)
        }
        write_love_tv.setOnClickListener{
            selectFeel(love_select_tv, FeelSet.LOVE)
        }
        write_shame_tv.setOnClickListener{
            selectFeel(shame_select_tv, FeelSet.SHAME)
        }
    }

    private fun selectFeel(tv : TextView, feel: FeelSet){
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