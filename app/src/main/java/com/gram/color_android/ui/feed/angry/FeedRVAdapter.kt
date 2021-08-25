package com.gram.color_android.ui.feed.angry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.PostListResponse
import com.gram.color_android.network.set.FeelSet
import kotlinx.android.synthetic.main.feed_item.view.*


class FeedRVAdapter(private val items : PostListResponse, private val feel : FeelSet) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            view.feed_name_tv.text = item.user_nickname
            view.feed_date_tv.text = item.created_at
            view.feed_content_tv.text = item.content
            view.feed_like_cnt_tv.text = item.favorite_cnt.toString()
            view.feed_comment_cnt_tv.text = item.comment_cnt.toString()
            if(item.hash_code != null) {
                view.feed_tag_tv.text = item.hash_code.toString()
            }
            if(item.is_favorite){
                view.feed_like_ib.isSelected = true
            }

            setLikeImage(view, feel)

            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemView.feed_more_iv.setOnClickListener{
                    moreListener?.onMoreClick(itemView, position)
                }
                itemView.feed_like_ib.setOnClickListener{
                    likeListener?.onLikeClick(itemView, position)
                }
                itemView.feed_comment_ib.setOnClickListener{
                    commentListener?.onCommentClick(itemView, position)
                }
            }
        }
    }

    private fun setLikeImage(view: View, feel: FeelSet) {
        when(feel) {
            FeelSet.ANGRY -> view.feed_like_ib.setImageResource(R.drawable.like_selector_angry)
            FeelSet.HAPPY -> view.feed_like_ib.setImageResource(R.drawable.like_selector_happy)
            FeelSet.SAD -> view.feed_like_ib.setImageResource(R.drawable.like_selector_sad)
            FeelSet.BORED -> view.feed_like_ib.setImageResource(R.drawable.like_selector_bored)
            FeelSet.SHAME -> view.feed_like_ib.setImageResource(R.drawable.like_selector_shame)
            FeelSet.LOVE -> view.feed_like_ib.setImageResource(R.drawable.like_selector_love)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView :View =
            LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
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

    fun removeItem(position: Int){
        items.postContentResponseList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}

