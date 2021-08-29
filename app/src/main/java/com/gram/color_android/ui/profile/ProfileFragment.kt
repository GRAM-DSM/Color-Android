package com.gram.color_android.ui.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.gram.color_android.R
import com.gram.color_android.data.model.profile.ModifyNameRequest
import com.gram.color_android.data.model.profile.ProfileResponse
import com.gram.color_android.databinding.FragmentProfileBinding
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.network.set.ProfileSet
import com.gram.color_android.ui.feed.FeedActivity
import com.gram.color_android.ui.sign.SignActivity
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.logout.*

class ProfileFragment : Fragment() {

    private lateinit var adapter : PostPagerAdapter
    private val prefs = SharedPreferencesHelper.getInstance()
    private val profileViewModel = ProfileViewModel()
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

        profileViewModel.getProfile(prefs.accessToken!!, prefs.email!!, feel.toString(), "post", 1)
        profileViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when(it){
                ProfileSet.GET_SUCCESS -> {
                    val userInfo = profileViewModel.postListLiveData.value!!.user_info
                    setProfile(userInfo)
                }
                ProfileSet.MODIFY_SUCCESS -> Toast.makeText(requireContext(), getString(R.string.modify_name_success), Toast.LENGTH_SHORT).show()
                ProfileSet.MODIFY_FAIL -> Toast.makeText(requireContext(), getString(R.string.do_not_change_name), Toast.LENGTH_SHORT).show()
            }
        })

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
            setEditText(true)
        }
        profile_cancel_btn.setOnClickListener{
            setEditText(false)
        }
        profile_logout_tv.setOnClickListener{
            logout()
        }
        profile_modify_btn.setOnClickListener{
            modifyName()
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

    private fun modifyName(){
        val body = ModifyNameRequest(profile_name_et.text.toString())
        profileViewModel.modifyName(prefs.accessToken!!, body)
        showKeyboard(false)
    }

    private fun setEditText(boolean: Boolean){
        if(boolean){
            profile_name_et.isEnabled = boolean
            profile_name_et.requestFocus()
            profile_name_et.isCursorVisible = boolean
            profile_name_et.setSelection(profile_name_et.length())
            profile_modify_name_iv.visibility = View.INVISIBLE
            profile_modify_btn.visibility = View.VISIBLE
            profile_cancel_btn.visibility = View.VISIBLE
            showKeyboard(true)
        }
        else {
            profile_name_et.setText(profileViewModel.postListLiveData.value!!.user_info.nickname)
            profile_name_et.isEnabled = boolean
            profile_name_et.isCursorVisible = boolean
            profile_name_et.setSelection(profile_name_et.length())
            profile_modify_name_iv.visibility = View.VISIBLE
            profile_modify_btn.visibility = View.INVISIBLE
            profile_cancel_btn.visibility = View.INVISIBLE
        }
    }

    private fun logout(){
        val dialog = Dialog(requireActivity())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.logout)
        dialog.show()

        dialog.logout_dialog_negative.setOnClickListener{
            dialog.dismiss()
        }
        dialog.logout_dialog_positive.setOnClickListener{
            dialog.dismiss()
            prefs.accessToken = null
            prefs.refreshToken = null

            val intent = Intent(requireActivity(), SignActivity::class.java)
            intent.apply {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
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

    private fun setProfile(userInfo : ProfileResponse.UserInfo) {
        profile_name_et.setText(userInfo.nickname)
        profile_user_color_tv.text = userInfo.nickname + getString(R.string.user_color)
    }

    private fun showKeyboard(isShow: Boolean){
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(isShow)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        else
            imm.hideSoftInputFromWindow(profile_name_et.windowToken, 0)
    }
}