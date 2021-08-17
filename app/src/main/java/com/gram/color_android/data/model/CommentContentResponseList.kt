package com.gram.color_android.data.model

data class CommentContentResponseList(val commentContentResponseList: ArrayList<Comment>){
    data class Comment(
        val id: String,
        val content: String,
        val created_at: String,
        val is_mine: Boolean,
        val user_nickname: String
    )
}


