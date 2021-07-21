package com.example.color_android.ui.sign

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.color_android.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_tv.setOnClickListener{
            (activity as SignActivity).replaceFragment(RegisterFragment())
        }

        layoutSetting()
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
}