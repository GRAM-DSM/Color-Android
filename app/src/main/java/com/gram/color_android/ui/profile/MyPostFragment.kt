package com.gram.color_android.ui.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.gram.color_android.network.set.ProfileSet
import com.gram.color_android.ui.feed.FeedActivity
import com.gram.color_android.ui.feed.angry.AngryCommentRVAdapter
import com.gram.color_android.ui.feed.angry.AngryFeedRVAdapter
import com.gram.color_android.ui.feed.angry.FeedAngryFragment
import com.gram.color_android.ui.sign.SignActivity
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.ColorApplication
import com.gram.color_android.util.OnBackPressedListener
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.FeedViewModel
import com.gram.color_android.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.comment_bottomsheet_other.*
import kotlinx.android.synthetic.main.feed_comment_bottomsheet.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_mine.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_other.*
import kotlinx.android.synthetic.main.feed_post_delete.*
import kotlinx.android.synthetic.main.fragment_feed_angry.*
import kotlinx.android.synthetic.main.fragment_my_post.*

class MyPostFragment : Fragment(), OnBackPressedListener {

    private lateinit var adapter: ProfileRVAdapter
    private lateinit var commentAdapter: AngryCommentRVAdapter
    private lateinit var feedDialog: BottomSheetDialog
    private lateinit var commentDialog: BottomSheetDialog
    private lateinit var commentBottomSheet: BottomSheetDialog
    private val profileViewModel = ProfileViewModel()
    private val feedViewModel = FeedViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()
    private var pos = 0
    private var id = ""

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
        swipeRefresh()

        feedDialog = BottomSheetDialog(requireContext())
        commentDialog = BottomSheetDialog(requireContext())
        commentBottomSheet = BottomSheetDialog(requireContext())

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when (it) {
                ProfileSet.GET_SUCCESS -> {
                    adapter = ProfileRVAdapter(profileViewModel.postListLiveData.value!!)
                    profile_my_post_rv.adapter = adapter
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
                        AngryCommentRVAdapter(feedViewModel.commentListLiveData.value!!)
                    commentBottomSheet.feed_comment_rv.adapter = commentAdapter
                    commentLongClick()
                }
                FeedSet.WRITE_SUCCESS -> {
                    getCommentList(id, 0)
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
                    showDeleteDialog(prefs.accessToken!!, id)
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
                getCommentList(id, 0)
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

    private fun commentLongClick() {
        commentAdapter.setOnItemLongClickListener(object :
            AngryCommentRVAdapter.OnItemLongClickListener {
            override fun onItemLongClick(v: View, position: Int) {
                val item =
                    feedViewModel.commentListLiveData.value!!.commentContentResponseList[position]
                commentDialog.setContentView(R.layout.comment_bottomsheet_mine)
                commentDialog.show()
            }
        })
    }

    private fun showDeleteDialog(header: String, post_id: String) {
        val deleteDialog = Dialog(requireContext())
        deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.setContentView(R.layout.feed_post_delete)
        feedDialog.dismiss()
        deleteDialog.show()

        deleteDialog.post_delete_dialog_cancel.setOnClickListener {
            deleteDialog.dismiss()
        }
        deleteDialog.post_delete_dialog_delete.setOnClickListener {
            feedViewModel.deletePost(header, post_id)
            deleteDialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as FeedActivity).setOnBackPressedListener(this)
    }

    override fun onBackPressed() {
        (activity as FeedActivity).replaceFragment(FeedAngryFragment())
    }

    private fun getPost() {
        profileViewModel.getProfile(
            prefs.accessToken!!,
            prefs.email!!,
            ColorApplication.feel,
            "post",
            1
        )
    }

    private fun getCommentList(id: String, page: Int) {
        feedViewModel.getCommentList(prefs.accessToken!!, id, page)
    }

    private fun swipeRefresh() {
        profile_my_post_refresh.setOnRefreshListener {
            getPost()
            profile_my_post_refresh.isRefreshing = false
        }
    }
}