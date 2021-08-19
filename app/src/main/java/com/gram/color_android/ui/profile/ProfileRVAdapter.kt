package com.gram.color_android.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.R
import com.gram.color_android.data.model.profile.ProfileResponse
import kotlinx.android.synthetic.main.feed_item.view.*

class ProfileRVAdapter(private val items : ProfileResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view = v
        fun bind(item : ProfileResponse.Posts){
            view.feed_name_tv.text = item.user_nickname
            view.feed_date_tv.text = item.created_at
            view.feed_content_tv.text = item.content
            view.feed_like_cnt_tv.text = item.favorite_cnt.toString()
            view.feed_comment_cnt_tv.text = item.comment_cnt.toString()
        }
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
}