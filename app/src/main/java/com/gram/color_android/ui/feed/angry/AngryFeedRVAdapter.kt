package com.gram.color_android.ui.feed.angry

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.data.model.PostListResponse


class AngryFeedRVAdapter(val items : PostListResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private val view = v
        fun bind(item : PostListResponse.PostContent){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}


