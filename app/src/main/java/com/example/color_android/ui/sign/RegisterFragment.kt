package com.example.color_android.ui.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.color_android.R
import com.example.color_android.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val registerViewModel = RegisterViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sign_close_ib.setOnClickListener{
            (activity as SignActivity).replaceFragment(LoginFragment())
        }

        sign_overlap_tv.setOnClickListener{
            registerViewModel.nameCheck(sign_name_et.text.toString())
        }
    }
}