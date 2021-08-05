package com.gram.color_android.data.model

data class PostListResponse(
    val total_pages : String,
    val PostContentResponseList : List<PostContent>
){
    data class PostContent(
        val id : Int,
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

