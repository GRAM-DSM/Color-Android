package com.example.color_android.ui.sign

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.color_android.R
import com.example.color_android.data.model.sign.RegisterRequest
import com.example.color_android.databinding.FragmentRegisterBinding
import com.example.color_android.network.set.RegisterSet
import com.example.color_android.util.OnBackPressedListener
import com.example.color_android.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(), OnBackPressedListener {

    private val registerViewModel = RegisterViewModel()
    private var dataBinding: FragmentRegisterBinding? = null
    private var certifyCheck = false
    private var nameOverlapCheck = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return dataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding!!.viewModel = registerViewModel
        editTextNullCheck()
        registerViewModel.registerLiveData.observe(viewLifecycleOwner, {
            when (it) {
                RegisterSet.NAME_SUCCESS -> {
                    Toast.makeText(activity, R.string.sign_available_nickname, Toast.LENGTH_SHORT).show()
                    nameOverlapCheck = true
                }
                RegisterSet.NAME_FAIL -> {
                    Toast.makeText(activity, R.string.sign_unavailable_nickname, Toast.LENGTH_SHORT).show()
                    nameOverlapCheck = false
                }
                RegisterSet.SEND_SUCCESS -> Toast.makeText(activity, getString(R.string.send_email), Toast.LENGTH_SHORT).show()
                RegisterSet.SEND_FAIL -> Toast.makeText(activity, getString(R.string.sign_error), Toast.LENGTH_SHORT).show()
                RegisterSet.EMAIL_SUCCESS -> {
                    sign_code_check_tv.text = getString(R.string.success_code)
                    certifyCheck = true
                }
                RegisterSet.EMAIL_FAIL -> {
                    sign_code_check_tv.text = getString(R.string.fail_code)
                    certifyCheck = false
                }
                RegisterSet.REGISTER_SUCCESS -> {
                    Toast.makeText(activity, getString(R.string.success_register), Toast.LENGTH_SHORT).show()
                    (activity as SignActivity).replaceFragment(LoginFragment())
                }
                RegisterSet.REGISTER_FAIL -> {
                    Toast.makeText(activity, getString(R.string.fail_register), Toast.LENGTH_SHORT).show()
                }
            }
        })
        sign_close_ib.setOnClickListener {
            (activity as SignActivity).replaceFragment(LoginFragment())
        }
        sign_up_btn.setOnClickListener {
            val user = getUserInfo()
            if (!user.email.equals("") && !user.password.equals("") && certifyCheck && nameOverlapCheck)
                registerViewModel.register(user)
        }
    }

    override fun onBackPressed() {
        (activity as SignActivity).replaceFragment(LoginFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as SignActivity).setOnBackPressedListener(this)
    }

    private fun editTextNullCheck() {
        sign_name_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sign_overlap_tv.isEnabled = !sign_name_et.text.toString().equals("")
            }
        })
        sign_email_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sign_send_tv.isEnabled = !sign_email_et.text.toString().equals("")
            }
        })
        sign_code_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sign_code_check_tv.isEnabled = !sign_code_et.text.toString().equals("")
            }
        })
    }

    private fun getUserInfo(): RegisterRequest {
        val email = sign_email_et.text.toString().trim()
        val password = sign_pass_et.text.toString().trim()
        val nickname = sign_name_et.text.toString().trim()

        return RegisterRequest(email, password, nickname)
    }
}