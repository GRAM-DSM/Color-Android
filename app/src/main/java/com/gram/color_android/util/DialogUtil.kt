package com.gram.color_android.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.FeedReportRequest
import com.gram.color_android.network.set.FeelSet
import com.gram.color_android.ui.feed.angry.CommentRVAdapter
import com.gram.color_android.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.comment_bottomsheet_other.*
import kotlinx.android.synthetic.main.report_dialog.*

class DialogUtil {

    companion object {
        private var instance: DialogUtil? = null

        fun getInstance(): DialogUtil {
            if (instance == null)
                instance = DialogUtil()
            return instance!!
        }
    }

    private val prefs = SharedPreferencesHelper.getInstance()
    private val feedViewModel = FeedViewModel()

    fun commentLongClick(adapter: CommentRVAdapter, context: Context, feel: FeelSet) {
        val commentDialog = BottomSheetDialog(context)

        adapter.setOnItemLongClickListener(object : CommentRVAdapter.OnItemLongClickListener {
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
                        showReportDialog(context ,item.id, "comment", feel)
                    }
                }
            }
        })
    }

    private fun showReportDialog(context: Context ,id: String, type: String, feel: FeelSet) {
        val reportDialog = Dialog(context)
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
                val body = FeedReportRequest(reason, feel.toString())
                feedViewModel.report(prefs.accessToken!!, body, id, type)
                reportDialog.dismiss()
            }
        }
    }
}