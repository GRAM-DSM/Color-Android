package com.gram.color_android.data.model.profile

data class ProfileResponse(
    val user_info: UserInfo,
    val posts: ArrayList<Posts>
){
    data class UserInfo(
        val nickname: String,
        val is_mine: Boolean
    )
    data class Posts(
        val id: String,
        val content: String,
        val is_mine: Boolean,
        val created_at: String,
        val user_nickname: String,
        val comment_cnt: Int,
        val favorite_cnt: Int,
        val is_favorite: Boolean,
        val hash_tag: ArrayList<String>
    )
}
