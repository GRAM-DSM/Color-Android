package com.gram.color_android.ui.sign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gram.color_android.R
import com.gram.color_android.ui.main.MainActivity
import com.gram.color_android.util.OnBackPressedListener
import com.gram.color_android.util.SharedPreferencesHelper

class SignActivity : AppCompatActivity() {
    private var listener : OnBackPressedListener? = null
    private val prefs = SharedPreferencesHelper.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.sign_fragment_container, LoginFragment()).commit()

        autoLogin()
    }

    private fun autoLogin(){
        if(prefs.isLogin && prefs.access_token != null){
            val intent = Intent(this@SignActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
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
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
        fragmentTransaction.replace(R.id.sign_fragment_container, fragment).commit()
    }
}