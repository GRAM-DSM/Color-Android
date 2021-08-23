package com.gram.color_android.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gram.color_android.R
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.network.set.ProfileSet
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_like_post.*

class LikePostFragment : Fragment() {

    private lateinit var adapter : ProfileRVAdapter
    private val profileViewModel = ProfileViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val linearLayoutManager = LinearLayoutManager(requireContext())
        profile_like_post_rv.layoutManager = linearLayoutManager

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when(it){
                ProfileSet.GET_SUCCESS -> {
                    adapter = ProfileRVAdapter(profileViewModel.postListLiveData.value!!, "like")
                    profile_like_post_rv.adapter = adapter
                }
            }
        })
    }

    private fun getPost(){
        profileViewModel.getProfile(prefs.accessToken!!, prefs.email!!, FeelSet.ANGRY.toString(), "like", 1)
    }
}