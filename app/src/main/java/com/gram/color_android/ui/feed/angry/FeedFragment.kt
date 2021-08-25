package com.gram.color_android.ui.feed.angry

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.CommentRequest
import com.gram.color_android.network.set.FeedSet
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.FeedActivity
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.DialogUtil
import com.gram.color_android.util.OnBackPressedListener
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.feed_comment_bottomsheet.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_mine.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_other.*
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment(), OnBackPressedListener {

    private lateinit var feedAdapter: AngryFeedRVAdapter
    private lateinit var commentAdapter: CommentRVAdapter
    private lateinit var feedDialog: BottomSheetDialog
    private lateinit var commentBottomSheet: BottomSheetDialog
    private val feedViewModel = FeedViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()
    private val dialogUtil = DialogUtil.getInstance()
    private var totalPage = 0
    private var feedPage = 0
    private val feel = FeedActivity.getFeel()
    private var pos = 0
    private var id = ""
    private var isPostLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorInit(feel)

        val linearLayoutManager = LinearLayoutManager(activity)
        feed_rv.layoutManager = linearLayoutManager

        feedDialog = BottomSheetDialog(requireContext())
        commentBottomSheet = BottomSheetDialog(requireContext())
        getPostList()
        getPostMore()
        swipeRefresh()

        feedViewModel.feedLiveData.observe(viewLifecycleOwner, {
            when (it) {
                FeedSet.GET_FEED_SUCCESS -> {
                    feedAdapter = AngryFeedRVAdapter(feedViewModel.feedListLiveData.value!!)
                    feed_rv.adapter = feedAdapter
                    totalPage = feedViewModel.feedListLiveData.value!!.totalPages
                    postBtnClick()
                }
                FeedSet.DELETE_SUCCESS -> feedAdapter.removeItem(pos)
                FeedSet.DELETE_FAIL -> Toast.makeText(
                    requireContext(),
                    getString(R.string.do_not_delete),
                    Toast.LENGTH_SHORT
                ).show()
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
                FeedSet.REPORT_SUCCESS -> Toast.makeText(
                    requireContext(),
                    getString(R.string.send_report),
                    Toast.LENGTH_SHORT
                ).show()
                FeedSet.GET_MORE_SUCCESS -> {
                    feedAdapter.notifyDataSetChanged()
                    isPostLoading = false
                }
                FeedSet.DELETE_COM_SUCCESS -> commentAdapter.removeItem(dialogUtil.getPosition()!!)
            }
        })
    }

    private fun colorInit(feel : FeelSet) {
        var position = 0
        when(feel){
            FeelSet.ANGRY -> position = 0
            FeelSet.HAPPY -> position = 1
            FeelSet.SAD -> position = 2
            FeelSet.BORED -> position = 3
            FeelSet.SHAME -> position = 4
            FeelSet.LOVE -> position = 5
        }
        val colors = arrayListOf(R.color.angry, R.color.happy, R.color.sad, R.color.bored, R.color.shame, R.color.love)
        feed_progressBar.indeterminateTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, colors[position], null))
    }

    private fun getPostList() {
        feedViewModel.getPostList(prefs.accessToken!!, getFeedPage(), feel.toString())
    }

    private fun getPostMore() {
        feed_rv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                if(!isPostLoading && feedViewModel.feedListLiveData.value!!.totalPages >= 1){
                    if(layoutManager != null &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1){
                            feed_progressBar.visibility = View.VISIBLE
                            requireActivity().runOnUiThread {
                                val handler = Handler(Looper.getMainLooper())
                                handler.postDelayed({
                                    feedViewModel.getPostMore(prefs.accessToken!!, getFeedPage(), feel.toString())
                                    isPostLoading = true
                                    feed_progressBar.visibility = View.INVISIBLE
                                }, 1000)
                            }
                    }
                }
            }
        })
    }

    private fun getCommentList(id: String) {
        feedViewModel.getCommentList(prefs.accessToken!!, id)
    }

    private fun getFeedPage(): Int = feedPage++

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
                val item =
                    feedViewModel.feedListLiveData.value!!.postContentResponseList[position]
                if (item.is_mine) {
                    feedDialog.setContentView(R.layout.feed_more_bottomsheet_mine)
                    feedDialog.delete_post_btn.setOnClickListener {
                        pos = position
                        id =
                            feedViewModel.feedListLiveData.value!!.postContentResponseList[position].id
                        dialogUtil.showDeletePost(requireContext(), prefs.accessToken!!, id)
                    }
                    feedDialog.modify_post_btn.setOnClickListener {
                        val intent = Intent(requireContext(), WriteActivity::class.java)
                        intent.apply {
                            intent.putExtra("content", item.content)
                            intent.putExtra("tag", item.hash_code.toString())
                            intent.putExtra("post_id", item.id)
                        }
                        startActivity(intent)
                    }
                } else {
                    feedDialog.setContentView(R.layout.feed_more_bottomsheet_other)
                    feedDialog.view_post_btn.text =
                        item.user_nickname + getString(R.string.view_other)
                    feedDialog.report_post_btn.setOnClickListener {
                        feedDialog.dismiss()
                        dialogUtil.showReportDialog(requireContext(), item.id, "post", feel)
                    }
                }
                feedDialog.show()
            }
        })
        feedAdapter.setOnLikeClickListener(object : AngryFeedRVAdapter.OnLikeClickListener {
            override fun onLikeClick(v: View, position: Int) {
                val item = feedViewModel.feedListLiveData.value!!.postContentResponseList[position]
                var like = Integer.parseInt(v.feed_like_cnt_tv.text.toString())

                if (v.feed_like_ib != null) {
                    when (v.feed_like_ib.isSelected) {
                        true -> v.feed_like_cnt_tv.text = (like - 1).toString()
                        false -> v.feed_like_cnt_tv.text = (like + 1).toString()
                    }
                    v.feed_like_ib.isSelected = !v.feed_like_ib.isSelected
                }

                feedViewModel.like(prefs.accessToken!!, item.id)
                pos = position
            }
        })
        feedAdapter.setOnCommentClickListener(object :
            AngryFeedRVAdapter.OnCommentClickListener {
            override fun onCommentClick(v: View, position: Int) {
                id = feedViewModel.feedListLiveData.value!!.postContentResponseList[position].id
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

    override fun onResume() {
        super.onResume()
        (activity as FeedActivity).setOnBackPressedListener(this)
    }

    override fun onBackPressed() {
        (activity as FeedActivity).finishAffinity()
    }
}