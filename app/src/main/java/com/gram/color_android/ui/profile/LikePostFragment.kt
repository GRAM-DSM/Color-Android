package com.gram.color_android.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.CommentRequest
import com.gram.color_android.network.set.FeedSet
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.network.set.ProfileSet
import com.gram.color_android.ui.feed.FeedActivity
import com.gram.color_android.ui.feed.angry.CommentRVAdapter
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.DialogUtil
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.FeedViewModel
import com.gram.color_android.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.feed_comment_bottomsheet.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_mine.*
import kotlinx.android.synthetic.main.fragment_like_post.*
import kotlinx.android.synthetic.main.fragment_my_post.*

class LikePostFragment(private var feel: FeelSet) : Fragment() {

    private lateinit var adapter : ProfileRVAdapter
    private lateinit var commentAdapter: CommentRVAdapter
    private lateinit var commentBottomSheet: BottomSheetDialog
    private lateinit var feedDialog: BottomSheetDialog
    private val profileViewModel = ProfileViewModel()
    private val feedViewModel = FeedViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()
    private val dialogUtil = DialogUtil.getInstance()
    private var pos = 0
    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedDialog = BottomSheetDialog(requireContext())
        commentBottomSheet = BottomSheetDialog(requireContext())

        getPost()
        swipeRefresh()

        val linearLayoutManager = LinearLayoutManager(requireContext())
        profile_like_post_rv.layoutManager = linearLayoutManager

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when(it){
                ProfileSet.GET_SUCCESS -> {
                    adapter = ProfileRVAdapter(profileViewModel.postListLiveData.value!!, feel)
                    profile_like_post_rv.adapter = adapter
                    postBtnClick()
                }
            }
        })
        feedViewModel.feedLiveData.observe(viewLifecycleOwner, {
            when (it) {
                FeedSet.DELETE_SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_post_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    adapter.removeItem(pos)
                }
                FeedSet.GET_COMMENT_SUCCESS -> {
                    commentAdapter =
                        CommentRVAdapter(feedViewModel.commentListLiveData.value!!)
                    commentBottomSheet.feed_comment_rv.adapter = commentAdapter
                    dialogUtil.commentLongClick(commentAdapter, feedViewModel, requireContext(), id, feel)
                }
                FeedSet.WRITE_SUCCESS -> {
                    getCommentList(id)
                    commentBottomSheet.feed_comment_et.setText("")
                }
            }
        })
    }

    private fun postBtnClick() {
        adapter.setOnMoreClickListener(object : ProfileRVAdapter.OnMoreClickListener {
            override fun onMoreClick(v: View, position: Int) {
                val item = profileViewModel.postListLiveData.value!!.posts[position]
                feedDialog.setContentView(R.layout.feed_more_bottomsheet_mine)
                feedDialog.delete_post_btn.setOnClickListener {
                    pos = position
                    id = profileViewModel.postListLiveData.value!!.posts[position].id
                    dialogUtil.showDeletePost(requireContext(), prefs.accessToken!!, id)
                    feedDialog.dismiss()
                }
                feedDialog.modify_post_btn.setOnClickListener {
                    feedDialog.dismiss()
                    val intent = Intent(requireContext(), WriteActivity::class.java)
                    intent.apply {
                        intent.putExtra("content", item.content)
                        intent.putExtra("tag", item.hash_tag)
                        intent.putExtra("post_id", item.id)
                    }
                    startActivity(intent)
                }
                feedDialog.show()
            }
        })
        adapter.setOnLikeClickListener(object : ProfileRVAdapter.OnLikeClickListener {
            override fun onLikeClick(v: View, position: Int) {
                val item = profileViewModel.postListLiveData.value!!.posts[position]
                var like = Integer.parseInt(v.feed_like_cnt_tv.text.toString())

                if(v.feed_like_ib != null){
                    when(v.feed_like_ib.isSelected){
                        true -> v.feed_like_cnt_tv.text = (like - 1).toString()
                        false -> v.feed_like_cnt_tv.text = (like + 1).toString()
                    }
                    v.feed_like_ib.isSelected = !v.feed_like_ib.isSelected
                }

                feedViewModel.like(prefs.accessToken!!, item.id)
                pos = position
            }
        })
        adapter.setOnCommentClickListener(object :
            ProfileRVAdapter.OnCommentClickListener {
            override fun onCommentClick(v: View, position: Int) {
                id = profileViewModel.postListLiveData.value!!.posts[position].id
                getCommentList(id)
                commentBottomSheet.setContentView(R.layout.feed_comment_bottomsheet)
                commentBottomSheet.feed_comment_rv.layoutManager =
                    LinearLayoutManager(requireContext())
                commentBottomSheet.show()

                commentBottomSheet.comment_send_ib.setOnClickListener {
                    feedViewModel.writeComment(
                        prefs.accessToken!!,
                        id,
                        CommentRequest(commentBottomSheet.feed_comment_et.text.toString())
                    )
                }
            }
        })
    }

    private fun getPost(){
        profileViewModel.getProfile(prefs.accessToken!!, prefs.email!!, feel.toString(), "like", 1)
    }

    private fun getCommentList(id: String) {
        feedViewModel.getCommentList(prefs.accessToken!!, id)
    }

    private fun swipeRefresh() {
        profile_like_refresh.setOnRefreshListener {
            getPost()
            profile_like_refresh.isRefreshing = false
        }
    }
}