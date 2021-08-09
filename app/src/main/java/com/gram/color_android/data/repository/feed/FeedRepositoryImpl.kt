package com.gram.color_android.data.repository.feed

import com.gram.color_android.data.model.feed.PostListResponse
import com.gram.color_android.network.RetrofitClient
import com.gram.color_android.network.SafeApiRequest
import retrofit2.Response

class FeedRepositoryImpl : FeedRepository, SafeApiRequest() {
    override suspend fun getPostList(
        header: String,
        page: Int,
        feel: String
    ): Response<PostListResponse> {
        return safeApiCall { RetrofitClient.getAPI().getPostList(header, page, feel) }
    }


}