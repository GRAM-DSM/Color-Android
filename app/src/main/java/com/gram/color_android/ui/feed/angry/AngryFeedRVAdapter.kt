package com.gram.color_android.ui.feed.angry

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.PostListResponse
import kotlinx.android.synthetic.main.angry_item.view.*


class AngryFeedRVAdapter(private val items : PostListResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener{
        fun onBtnClick(v: View, position: Int)
    }

    private var listener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private val view = v
        fun bind(item : PostListResponse.PostContent){
            view.angry_feed_name.text = item.user_nickname
            view.angry_feed_date.text = item.created_at
            view.angry_feed_content_tv.text = item.content
            view.angry_like_cnt_tv.text = item.favorite_cnt.toString()
            view.angry_comment_cnt_tv.text = item.comment_cnt.toString()

            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemView.angry_feed_more_iv.setOnClickListener{
                    listener?.onBtnClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView :View =
            LayoutInflater.from(parent.context).inflate(R.layout.angry_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.postContentResponseList[position]
        if(holder is ViewHolder){
            holder.apply {
                bind(item)
            }
        }
    }

    override fun getItemCount(): Int = items.postContentResponseList.size
}

