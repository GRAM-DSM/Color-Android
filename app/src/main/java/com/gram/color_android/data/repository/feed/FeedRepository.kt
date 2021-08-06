package com.gram.color_android.data.repository.feed

import com.gram.color_android.data.model.feed.PostListResponse
import retrofit2.Response

interface FeedRepository {
    suspend fun getPostList(header: String, page: Int, feel: String) : Response<PostListResponse>
}