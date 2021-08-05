package com.gram.color_android.ui.feed.angry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.R
import com.gram.color_android.data.model.PostListResponse
import kotlinx.android.synthetic.main.angry_item.view.*


class AngryFeedRVAdapter(val items : PostListResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private val view = v
        fun bind(item : PostListResponse.PostContent){
            view.angry_feed_name.text = item.user_nickname
            view.angry_feed_date.text = item.created_at
            view.angry_feed_content_tv.text = item.content
            view.angry_like_cnt_tv.text = item.favorite_cnt.toString()
            view.angry_comment_cnt_tv.text = item.comment_cnt.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView :View
        inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.angry_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.PostContentResponseList[position]
        if(holder is ViewHolder){
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = items.PostContentResponseList.size
}


