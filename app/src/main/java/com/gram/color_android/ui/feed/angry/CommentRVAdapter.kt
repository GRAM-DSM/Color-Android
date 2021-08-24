package com.gram.color_android.ui.feed.angry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.R
import com.gram.color_android.data.model.feed.CommentContentResponseList
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentRVAdapter(private val items: CommentContentResponseList) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemLongClickListener{
        fun onItemLongClick(v: View, position: Int)
    }

    private var longClickListener : OnItemLongClickListener? = null

    fun setOnItemLongClickListener(longClickListener: OnItemLongClickListener){
        this.longClickListener = longClickListener
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v
        fun bind(item : CommentContentResponseList.Comment){
            view.comment_name_tv.text = item.user_nickname
            view.comment_date_tv.text = item.created_at
            view.comment_contents_tv.text = item.content

            itemView.setOnLongClickListener(View.OnLongClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    longClickListener?.onItemLongClick(itemView, position)
                }
                return@OnLongClickListener true
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView : View =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.commentContentResponseList[position]
        if(holder is ViewHolder){
            holder.apply {
                bind(item)
            }
        }
    }

    override fun getItemCount() = items.commentContentResponseList.size

    fun removeItem(position: Int){
        items.commentContentResponseList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }
}


