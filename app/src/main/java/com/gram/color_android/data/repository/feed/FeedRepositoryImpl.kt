package com.gram.color_android.data.repository.feed

import com.gram.color_android.data.model.CommentContentResponseList
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
        return safeApiCall { RetrofitClient.getSpringAPI().getPostList(header, page, feel) }
    }

    override suspend fun deletePost(header: String, post_id: String): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().deletePost(header, post_id) }
    }

//    override suspend fun like(header: String, post_id: Int): Response<Void> {
//        return safeApiCall { RetrofitClient.getFastAPI().like(header, post_id) }
//    }

    override suspend fun getCommentList(
        header: String,
        post_id: String,
        page: Int
    ): Response<CommentContentResponseList> {
        return safeApiCall { RetrofitClient.getSpringAPI().getComment(header, post_id, page) }
    }
}