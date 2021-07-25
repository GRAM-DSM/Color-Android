package com.example.color_android.ui.sign

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.color_android.R
import com.example.color_android.data.model.sign.LoginRequest
import com.example.color_android.network.set.LoginSet
import com.example.color_android.ui.main.MainActivity
import com.example.color_android.util.OnBackPressedListener
import com.example.color_android.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), OnBackPressedListener {

    private val loginViewModel = LoginViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate( R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutSetting()
        loginViewModel.loginLiveData.observe(viewLifecycleOwner, {
            when(it){
                LoginSet.LOGIN_SUCCESS -> {
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                LoginSet.LOGIN_FAIL -> Toast.makeText(activity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
            }
        })
        sign_tv.setOnClickListener{
            (activity as SignActivity).replaceFragment(RegisterFragment())
        }
        login_btn.setOnClickListener{
            login(login_email_et.text.toString(), login_pass_et.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as SignActivity).setOnBackPressedListener(this)
    }

    override fun onBackPressed() {
        (activity as SignActivity).finish()
    }

    private fun layoutSetting(){
        val colorText = explain_tv.text
        val signUpText = sign_tv.text
        val content1 = SpannableString(colorText)
        val content2 = SpannableString(signUpText)

        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.angry, null)), 11, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.love, null)), 12, 13, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.happy, null)), 13, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.sad, null)), 14, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content1.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.bored, null)), 15, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        content2.setSpan(UnderlineSpan(), 0, signUpText.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        explain_tv.text = content1
        sign_tv.text = content2
    }

    private fun login(email: String, password: String){
        if(!email.equals("") && !password.equals(""))
            loginViewModel.login(LoginRequest(email, password))
    }
}