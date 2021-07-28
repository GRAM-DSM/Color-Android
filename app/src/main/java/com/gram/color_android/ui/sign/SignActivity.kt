package com.gram.color_android.ui.sign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gram.color_android.R
import com.gram.color_android.util.OnBackPressedListener

class SignActivity : AppCompatActivity() {
    private var listener : OnBackPressedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.sign_fragment_container, LoginFragment()).commit()
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
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
        fragmentTransaction.replace(R.id.sign_fragment_container, fragment).commit()
    }
}