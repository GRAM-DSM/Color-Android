package com.gram.color_android.ui.profile

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.gram.color_android.R
import com.gram.color_android.databinding.FragmentProfileBinding
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.FeedActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var adapter : PostPagerAdapter
    private var dataBinding: FragmentProfileBinding? = null
    private var feel: FeelSet? = FeedActivity.getFeel()
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

        adapter = PostPagerAdapter(requireActivity(), feel!!)
        profile_post_vp.adapter = adapter
        TabLayoutMediator(profile_tablayout, profile_post_vp) {tab, position ->
            when(position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_my_page)
                    setTint(tab.icon!!)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_like_empty)
                    setTint(tab.icon!!)
                }
            }
        }.attach()
        profile_tablayout.setSelectedTabIndicatorColor(resources.getColor(getFeelColor(feel!!), null))

        profile_modify_name_iv.setOnClickListener{
            profile_name_et.isEnabled = true
            profile_name_et.requestFocus()
        }
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

    private fun getFeelColor(feel: FeelSet) : Int{
        val colors = arrayListOf(R.color.angry, R.color.happy, R.color.sad, R.color.bored, R.color.shame, R.color.love)
        var position = when(feel){
            FeelSet.ANGRY -> 0
            FeelSet.HAPPY -> 1
            FeelSet.SAD -> 2
            FeelSet.BORED -> 3
            FeelSet.SHAME -> 4
            FeelSet.LOVE -> 5
        }
        return colors[position]
    }

    private fun setTint(drawable: Drawable){
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, resources.getColor(getFeelColor(feel!!), null))
    }

    private fun showKeyboard(){
    }
}