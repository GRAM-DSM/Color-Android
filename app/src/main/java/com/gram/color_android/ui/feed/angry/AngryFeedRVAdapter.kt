package com.gram.color_android.ui.feed.angry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.PostListResponse
import kotlinx.android.synthetic.main.angry_item.view.*


class AngryFeedRVAdapter(private val items : PostListResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnMoreClickListener{
        fun onMoreClick(v: View, position: Int)
    }
    interface OnLikeClickListener{
        fun onLikeClick(v: View, position: Int)
    }
    interface OnCommentClickListener{
        fun onCommentClick(v: View, position: Int)
    }

    private var moreListener: OnMoreClickListener? = null
    private var likeListener: OnLikeClickListener? = null
    private var commentListener: OnCommentClickListener? = null

    fun setOnMoreClickListener(moreListener: OnMoreClickListener){
        this.moreListener = moreListener
    }
    fun setOnLikeClickListener(likeListener: OnLikeClickListener){
        this.likeListener = likeListener
    }
    fun setOnCommentClickListener(commentListener: OnCommentClickListener){
        this.commentListener = commentListener
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
                    moreListener?.onMoreClick(itemView, position)
                }
                itemView.angry_like_ib.setOnClickListener{
                    likeListener?.onLikeClick(itemView, position)
                }
                itemView.angry_comment_ib.setOnClickListener{
                    commentListener?.onCommentClick(itemView, position)
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


