package com.gram.color_android.ui.profile

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.ColorFactory
import com.gram.color_android.R
import com.gram.color_android.data.model.profile.ProfileResponse
import com.gram.color_android.network.set.FeelSet
import kotlinx.android.synthetic.main.feed_item.view.*

class ProfileRVAdapter(private val items : ProfileResponse, private val feel : FeelSet) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view = v
        fun bind(item : ProfileResponse.Posts){
            view.feed_name_tv.text = item.user_nickname
            view.feed_date_tv.text = item.created_at
            view.feed_content_tv.text = item.content
            view.feed_like_cnt_tv.text = item.favorite_cnt.toString()
            view.feed_comment_cnt_tv.text = item.comment_cnt.toString()
            tagGroupAttr(view)
            view.tag_group.tags = item.hash_tag
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
    private fun tagGroupAttr(view: View){
        view.tag_group.theme = ColorFactory.NONE
        view.tag_group.backgroundColor = Color.WHITE
        view.tag_group.tagTextColor = Color.WHITE
        view.tag_group.tagBackgroundColor = Color.parseColor(getFeelColor(feel))
        view.tag_group.tagBorderColor = Color.parseColor(getFeelColor(feel))
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

    private fun getFeelColor(feel: FeelSet) : String{
        val colors = arrayListOf("#FF7473", "#FDD692", "#3998DD", "#BDBDBD", "#353866", "#FEC9C9")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView : View =
            LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.posts[position]
        if(holder is ViewHolder){
            holder.apply {
               bind(item)
            }
        }
    }

    override fun getItemCount(): Int = items.posts.size

    fun removeItem(position: Int){
        items.posts.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}