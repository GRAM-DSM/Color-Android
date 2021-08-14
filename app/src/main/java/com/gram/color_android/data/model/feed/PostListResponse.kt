package com.gram.color_android.data.model.feed

data class PostListResponse(
    val totalPages : String,
    val postContentResponseList : ArrayList<PostContent>
){
    data class PostContent(
        val id : String,
        val content: String,
        val is_mine: Boolean,
        val created_at: String,
        val user_nickname: String,
        val comment_cnt: Int,
        val favorite_cnt: Int,
        val is_favorite: Boolean,
        val hash_code: List<String>
    )
}

