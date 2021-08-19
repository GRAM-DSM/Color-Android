package com.gram.color_android.data.repository.feed

import com.gram.color_android.data.model.CommentContentResponseList
import com.gram.color_android.data.model.feed.CommentRequest
import com.gram.color_android.data.model.feed.FeedReportRequest
import com.gram.color_android.data.model.feed.PostListResponse
import retrofit2.Response

interface FeedRepository {
    suspend fun getPostList(header: String, page: Int, feel: String) : Response<PostListResponse>

    suspend fun deletePost(header: String, post_id: String) : Response<Void>

    //suspend fun like(header: String, post_id: Int) : Response<Void>

    suspend fun getCommentList(header: String, post_id: String, page: Int) : Response<CommentContentResponseList>

    suspend fun writeComment(header: String, post_id: String, body: CommentRequest) : Response<Void>

    suspend fun report(header: String, body: FeedReportRequest, id: String, type: String) : Response<Void>
}