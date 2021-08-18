package com.gram.color_android.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.gram.color_android.R
import com.gram.color_android.databinding.FragmentProfileBinding
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.FeedActivity
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private var dataBinding: FragmentProfileBinding? = null
    private var feel: FeelSet? = null
    private var curView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return dataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding!!.activity = ProfileFragment()

        profile_post_vp.adapter = PostPagerAdapter(requireActivity())
        TabLayoutMediator(profile_tablayout, profile_post_vp) {tab, position ->
            when(position) {
                0 -> tab.text = "탭1"
                1 -> tab.text = "탭2"
            }
        }.attach()
    }

    fun selectFeel(tv: TextView, feel: FeelSet) {
        if (curView == null) {
            curView = tv
            curView!!.visibility = View.VISIBLE
        } else {
            curView!!.visibility = View.GONE
            curView = tv
            curView!!.visibility = View.VISIBLE
        }
        this.feel = feel
    }
}