package com.gram.color_android.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.viewmodel.ProfileViewModel

class PostPagerAdapter(fragmentActivity: FragmentActivity, private val feel: FeelSet) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyPostFragment(feel)
            else -> LikePostFragment(feel)
        }
    }
}