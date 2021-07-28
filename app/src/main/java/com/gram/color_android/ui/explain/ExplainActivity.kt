package com.gram.color_android.ui.explain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gram.color_android.R
import com.gram.color_android.ui.sign.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*

class ExplainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explain)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.explain_fragment_container, AngryFragment()).commit()
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.explain_fragment_container, fragment).commit()
    }
}