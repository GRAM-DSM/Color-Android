package com.gram.color_android.data.model.feed

data class ProfileResponse(
    val user_info: UserInfo,
    val posts: List<Posts>
){
    data class UserInfo(
        val nickname: String,
        val is_mine: Boolean
    )
    data class Posts(
        val id: String,
        val content: String,
        val created_at: String,
        val user_nickname: String,
        val comment_cnt: Int,
        val favorite_cnt: Int,
        val is_favorite: Boolean,
        val hash_tag: List<String>
    )
}
