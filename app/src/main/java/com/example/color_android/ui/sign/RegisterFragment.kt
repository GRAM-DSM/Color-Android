package com.example.color_android.ui.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.color_android.R
import com.example.color_android.network.RegisterSet
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

        registerViewModel.registerLiveData.observe(viewLifecycleOwner, {
            when(it){
                RegisterSet.NameSuccess -> Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                RegisterSet.SendEmailSuccess -> Toast.makeText(activity, "Send Email", Toast.LENGTH_SHORT).show()
            }
        })

        sign_close_ib.setOnClickListener{
            (activity as SignActivity).replaceFragment(LoginFragment())
        }

        sign_overlap_tv.setOnClickListener{
            registerViewModel.nameCheck(sign_name_et.text.toString())
        }

        sign_send_tv.setOnClickListener{
            registerViewModel.sendEmail(sign_email_et.text.toString())
        }
    }
}