package com.gram.color_android.ui.feed.angry

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
import com.gram.color_android.network.set.FeedSet
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.ColorApplication
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.feed_comment_bottomsheet.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_mine.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_other.*
import kotlinx.android.synthetic.main.feed_post_delete.*
import kotlinx.android.synthetic.main.fragment_feed_angry.*

class FeedAngryFragment : Fragment() {

    private lateinit var feedAdapter : AngryFeedRVAdapter
    private lateinit var commentAdapter : AngryCommentRVAdapter
    private lateinit var feedBottomSheet: BottomSheetDialog
    private lateinit var commentBottomSheet: BottomSheetDialog
    private lateinit var deleteDialog : Dialog
    private val feedViewModel = FeedViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()
    private var feedPage = 0
    private var commentPage = 0
    private var isLike : ArrayList<Boolean> = ArrayList()
    private var pos = 0
    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_angry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ColorApplication.feel = FeelSet.ANGRY.toString()

        val linearLayoutManager = LinearLayoutManager(activity)
        feed_angry_rv.layoutManager = linearLayoutManager

        feedBottomSheet = BottomSheetDialog(requireContext())
        commentBottomSheet = BottomSheetDialog(requireContext())

        getPostList()
        swipeRefresh()

        feedViewModel.feedLiveData.observe(viewLifecycleOwner, {
            when(it){
                FeedSet.GET_FEED_SUCCESS -> {
                    feedAdapter = AngryFeedRVAdapter(feedViewModel.feedListLiveData.value!!)
                    feed_angry_rv.adapter = feedAdapter
                    postBtnClick()
                }
                FeedSet.DELETE_SUCCESS -> {
                    Toast.makeText(requireContext(), getString(R.string.delete_post_success), Toast.LENGTH_SHORT).show()
                    feedAdapter.removeItem(pos)
                }
                FeedSet.DELETE_FAIL -> Toast.makeText(requireContext(), getString(R.string.do_not_delete), Toast.LENGTH_SHORT).show()
                FeedSet.GET_COMMENT_SUCCESS -> {
                    commentAdapter = AngryCommentRVAdapter(feedViewModel.commentListLiveData.value!!)
                    commentBottomSheet.feed_comment_rv.adapter = commentAdapter
                }
                FeedSet.WRITE_SUCCESS -> {
                    getCommentList(id, 0)
                }
            }
        })
    }

    private fun showDeleteDialog(header: String, post_id: String) {
        deleteDialog = Dialog(requireContext())
        deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.setContentView(R.layout.feed_post_delete)
        feedBottomSheet.dismiss()
        deleteDialog.show()

        deleteDialog.post_delete_dialog_cancel.setOnClickListener{
            deleteDialog.dismiss()
        }
        deleteDialog.post_delete_dialog_delete.setOnClickListener{
            feedViewModel.deletePost(header, post_id)
            deleteDialog.dismiss()
        }
    }

    private fun getPostList(){
        feedViewModel.getPostList(prefs.accessToken!!, getFeedPage(), FeelSet.ANGRY.toString())
    }

    private fun getFeedPage(): Int = feedPage++

    private fun getCommentList(id: String, page: Int){
        feedViewModel.getCommentList(prefs.accessToken!!, id, page)
    }

    private fun getCommentPage(): Int = commentPage++

    private fun swipeRefresh() {
        swipe_refresh.setOnRefreshListener {
            feedPage = 0
            getPostList()
            swipe_refresh.isRefreshing = false
        }
    }

    private fun postBtnClick() {
        feedAdapter.setOnMoreClickListener(object : AngryFeedRVAdapter.OnMoreClickListener {
            override fun onMoreClick(v: View, position: Int) {
                val item = feedViewModel.feedListLiveData.value!!.postContentResponseList[position]
                if (item.is_mine) {
                    feedBottomSheet.setContentView(R.layout.feed_more_bottomsheet_mine)
                    feedBottomSheet.delete_post_btn.setOnClickListener {
                        pos = position
                        id = feedViewModel.feedListLiveData.value!!.postContentResponseList[position].id
                        showDeleteDialog(prefs.accessToken!!, id)
                    }
                    feedBottomSheet.modify_post_btn.setOnClickListener{
                        val intent = Intent(requireContext(), WriteActivity::class.java)
                        intent.apply {
                            intent.putExtra("content", item.content)
                            intent.putExtra("tag", item.hash_code.toString())
                            intent.putExtra("post_id", item.id)
                        }
                        startActivity(intent)
                    }
                } else {
                    feedBottomSheet.setContentView(R.layout.feed_more_bottomsheet_other)
                    feedBottomSheet.view_post_btn.text = item.user_nickname + getString(R.string.view_other)
                }
                feedBottomSheet.show()
            }
        })
        feedAdapter.setOnLikeClickListener(object : AngryFeedRVAdapter.OnLikeClickListener {
            override fun onLikeClick(v: View, position: Int) {
                if (!isLike[position]) {
                    v.feed_like_ib.setImageResource(R.drawable.ic_like_fill)
                    isLike[position] = true
                } else {
                    v.feed_like_ib.setImageResource(R.drawable.ic_like_empty)
                    isLike[position] = false
                }
            }
        })
        feedAdapter.setOnCommentClickListener(object : AngryFeedRVAdapter.OnCommentClickListener {
            override fun onCommentClick(v: View, position: Int) {
                id = feedViewModel.feedListLiveData.value!!.postContentResponseList[position].id
                getCommentList(id, 0)
                commentBottomSheet.setContentView(R.layout.feed_comment_bottomsheet)
                commentBottomSheet.feed_comment_rv.layoutManager = LinearLayoutManager(requireContext())
                commentBottomSheet.show()

                commentBottomSheet.comment_send_ib.setOnClickListener{
                    feedViewModel.writeComment(prefs.accessToken!!, id, commentBottomSheet.feed_comment_et.text.toString())
                }
            }
        })
    }
}