package com.gram.color_android.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gram.color_android.R
import com.gram.color_android.network.set.ProfileSet
import com.gram.color_android.util.ColorApplication
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_my_post.*

class MyPostFragment : Fragment() {

    private lateinit var adapter : ProfileRVAdapter
    private val profileViewModel = ProfileViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        profile_my_post_rv.layoutManager = linearLayoutManager

        getPost()

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when (it) {
                ProfileSet.GET_SUCCESS -> {
                    adapter = ProfileRVAdapter(profileViewModel.profileListLiveData.value!!)
                    profile_my_post_rv.adapter = adapter
                }
                ProfileSet.GET_FAIL -> {
                    Toast.makeText(requireContext(), "asdasdasdasdasdasd", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun getPost(){
        profileViewModel.getProfilePost(prefs.accessToken!!, prefs.email!!, ColorApplication.feel, "post", 1)
    }
}