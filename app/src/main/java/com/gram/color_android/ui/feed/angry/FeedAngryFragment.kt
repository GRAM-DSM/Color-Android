package com.gram.color_android.ui.feed.angry

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.CommentRequest
import com.gram.color_android.data.model.feed.FeedReportRequest
import com.gram.color_android.network.set.FeedSet
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.FeedActivity
import com.gram.color_android.ui.write.WriteActivity
import com.gram.color_android.util.ColorApplication
import com.gram.color_android.util.OnBackPressedListener
import com.gram.color_android.util.SharedPreferencesHelper
import com.gram.color_android.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.comment_bottomsheet_other.*
import kotlinx.android.synthetic.main.feed_comment_bottomsheet.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_mine.*
import kotlinx.android.synthetic.main.feed_more_bottomsheet_other.*
import kotlinx.android.synthetic.main.feed_post_delete.*
import kotlinx.android.synthetic.main.fragment_feed_angry.*
import kotlinx.android.synthetic.main.report_dialog.*

class FeedAngryFragment : Fragment(), OnBackPressedListener {

    private lateinit var feedAdapter: AngryFeedRVAdapter
    private lateinit var commentAdapter: AngryCommentRVAdapter
    private lateinit var feedDialog: BottomSheetDialog
    private lateinit var commentDialog: BottomSheetDialog
    private lateinit var commentBottomSheet: BottomSheetDialog
    private lateinit var reportDialog: Dialog
    private val feedViewModel = FeedViewModel()
    private val prefs = SharedPreferencesHelper.getInstance()
    private var totalPage = 0
    private var feedPage = 0
    private var commentPage = 0
    private var pos = 0
    private var id = ""
    private var isLoading = false

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

        feedDialog = BottomSheetDialog(requireContext())
        commentDialog = BottomSheetDialog(requireContext())
        commentBottomSheet = BottomSheetDialog(requireContext())

        getPostList()
        getPostMore()
        swipeRefresh()

        feedViewModel.feedLiveData.observe(viewLifecycleOwner, {
            when (it) {
                FeedSet.GET_FEED_SUCCESS -> {
                    feedAdapter = AngryFeedRVAdapter(feedViewModel.feedListLiveData.value!!)
                    feed_angry_rv.adapter = feedAdapter
                    totalPage = feedViewModel.feedListLiveData.value!!.totalPages
                    postBtnClick()
                }
                FeedSet.DELETE_SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_post_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    feedAdapter.removeItem(pos)
                }
                FeedSet.DELETE_FAIL -> Toast.makeText(
                    requireContext(),
                    getString(R.string.do_not_delete),
                    Toast.LENGTH_SHORT
                ).show()
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
                FeedSet.REPORT_SUCCESS -> Toast.makeText(
                    requireContext(),
                    getString(R.string.send_report),
                    Toast.LENGTH_SHORT
                ).show()
                FeedSet.GET_MORE_SUCCESS -> {
                    feedAdapter.notifyDataSetChanged()
                    isLoading = false
                }
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

    private fun showReportDialog(id: String, type: String) {
        reportDialog = Dialog(requireContext())
        reportDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        reportDialog.setContentView(R.layout.report_dialog)
        reportDialog.show()

        var reason = ""
        reportDialog.radio_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                reportDialog.radio_btn1.id -> reason =
                    reportDialog.reason1_tv.text.toString()
                reportDialog.radio_btn2.id -> reason =
                    reportDialog.reason2_tv.text.toString()
                reportDialog.radio_btn3.id -> reason =
                    reportDialog.reason3_tv.text.toString()
                reportDialog.radio_btn4.id -> reason =
                    reportDialog.reason4_tv.text.toString()
            }
        }

        reportDialog.report_negative_btn.setOnClickListener {
            reportDialog.dismiss()
        }
        reportDialog.report_positive_btn.setOnClickListener {
            if (reportDialog.confirm_radio_btn.isChecked) {
                val body = FeedReportRequest(reason, FeelSet.ANGRY.toString())
                report(body, id, type)
                reportDialog.dismiss()
            }
        }
    }

    private fun getPostList() {
        feedViewModel.getPostList(prefs.accessToken!!, getFeedPage(), FeelSet.ANGRY.toString())
    }

    private fun getPostMore() {
        feed_angry_rv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                if(!isLoading && feedViewModel.feedListLiveData.value!!.totalPages >= 1){
                    if(layoutManager != null &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1){
                            feed_progressBar.visibility = View.VISIBLE
                            requireActivity().runOnUiThread {
                                val handler = Handler(Looper.getMainLooper())
                                handler.postDelayed({
                                    feedViewModel.getPostMore(prefs.accessToken!!, getFeedPage(), FeelSet.ANGRY.toString())
                                    isLoading = true
                                    feed_progressBar.visibility = View.INVISIBLE
                                }, 1000)
                            }
                    }
                }
            }
        })
    }

    private fun getFeedPage(): Int = feedPage++

    private fun getCommentList(id: String, page: Int) {
        feedViewModel.getCommentList(prefs.accessToken!!, id, page)
    }

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
                        showDeleteDialog(prefs.accessToken!!, id)
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
                        showReportDialog(item.id, "post")
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
                if (item.is_mine) {
                    commentDialog.setContentView(R.layout.comment_bottomsheet_mine)
                    commentDialog.show()
                } else {
                    commentDialog.setContentView(R.layout.comment_bottomsheet_other)
                    commentDialog.show()

                    commentDialog.report_comment_btn.setOnClickListener {
                        commentDialog.dismiss()
                        showReportDialog(item.id, "comment")
                    }
                }
            }
        })
    }

    private fun report(body: FeedReportRequest, id: String, type: String) {
        feedViewModel.report(prefs.accessToken!!, body, id, type)
    }

    override fun onResume() {
        super.onResume()
        (activity as FeedActivity).setOnBackPressedListener(this)
    }

    override fun onBackPressed() {
        (activity as FeedActivity).finishAffinity()
    }
}
