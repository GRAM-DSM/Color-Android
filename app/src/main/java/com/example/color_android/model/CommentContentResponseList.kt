package com.example.color_android.model

data class CommentContentResponseList(val CommentContentResponseList: List<Comment>)

data class Comment(
    val id: Int,
    val content: String,
    val created_at: String,
    val is_mine: Boolean,
    val user_nickname: String
)