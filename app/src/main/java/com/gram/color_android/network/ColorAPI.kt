package com.gram.color_android.network

import com.gram.color_android.data.model.*
import com.gram.color_android.data.model.feed.FeedReportRequest
import com.gram.color_android.data.model.feed.MyPageResponse
import com.gram.color_android.data.model.feed.PostListResponse
import com.gram.color_android.data.model.sign.LoginRequest
import com.gram.color_android.data.model.sign.LoginResponse
import com.gram.color_android.data.model.sign.RegisterRequest
import retrofit2.Response
import retrofit2.http.*

interface ColorAPI {
    @POST("/auth")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @PUT("/auth")
    suspend fun tokenRefresh(@Header("Authorization") header: String): Response<String>

    @POST("/user")
    suspend fun register(@Body body: RegisterRequest): Response<Void>

    @HEAD("/user/{nickname}")
    suspend fun nameCheck(@Path("nickname") nickname: HashMap<String, String>): Response<Void>

    @POST("/email")
    suspend fun sendEmail(@Body body: HashMap<String, String>): Response<Void>

    @HEAD("/email/{email}/{code}")
    suspend fun emailCertify(
        @Path("email") email: String,
        @Path("code") code: String
    ): Response<Void>

    @POST("/post")
    suspend fun writing(
        @Header("Authorization") header: String,
        @Body body: WritingRequest
    ): Response<Void>

    @DELETE("/post/{path}")
    suspend fun deletePost(
        @Header("Authorization") header: String,
        @Path("post_id") post_id: Int
    ): Response<Void>

    @PUT("/post/{path}")
    suspend fun updatePost(
        @Header("Authorization") header: String,
        @Path("post_id") post_id: Int,
        @Body body: WritingRequest
    ): Response<Void>

    @GET("/post")
    suspend fun getPostList(
        @Header("Authorization") header: String,
        @Query("page") page: Int,
        @Query("feel") feel: String
    ): Response<PostListResponse>

    @GET("/comment")
    suspend fun getComment(
        @Header("Authorization") header: String,
        @Query("page") page: Int,
        @Query("post_id") post_id: Int
    ): Response<CommentContentResponseList>

    @DELETE("/comment/{comment_id}")
    suspend fun deleteComment(
        @Header("Authorization") header: String,
        @Path("comment_id") comment_id: Int
    ): Response<Void>

    @PUT("/like")
    suspend fun like(
        @Header("Authorization") header: String,
        @Query("post_id") post_id: Int
    ): Response<Void>

    @POST("/report")
    suspend fun report(
        @Header("Authorization") header: String,
        @Body body: FeedReportRequest,
        @Query("id") id: String,
        @Query("type") type: String
    ): Response<Void>

    @GET("/profile")
    suspend fun myPage(
        @Header("Authorization") header: String,
        @Query("id") id: String,
        @Query("feel") feel: String,
        @Query("filter") filter: String,
        @Query("page") page: Int
    ): Response<MyPageResponse>
}