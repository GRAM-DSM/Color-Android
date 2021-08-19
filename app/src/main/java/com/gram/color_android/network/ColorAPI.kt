package com.gram.color_android.network

import com.gram.color_android.data.model.*
import com.gram.color_android.data.model.feed.FeedReportRequest
import com.gram.color_android.data.model.profile.ProfileResponse
import com.gram.color_android.data.model.feed.PostListResponse
import com.gram.color_android.data.model.sign.LoginRequest
import com.gram.color_android.data.model.sign.LoginResponse
import com.gram.color_android.data.model.sign.RegisterRequest
import com.gram.color_android.data.model.sign.TokenRefreshResponse
import retrofit2.Response
import retrofit2.http.*

interface ColorAPI {
    @POST("/auth")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @PUT("/auth")
    suspend fun tokenRefresh(@Header("Refresh-Token") header: String): Response<TokenRefreshResponse>

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
    suspend fun write(
        @Header("Authorization") header: String,
        @Body body: WriteRequest
    ): Response<Void>

    @DELETE("/post/{post_id}")
    suspend fun deletePost(
        @Header("Authorization") header: String,
        @Path("post_id") post_id: String
    ): Response<Void>

    @PUT("/post/{path}")
    suspend fun updatePost(
        @Header("Authorization") header: String,
        @Path("post_id") post_id: Int,
        @Body body: WriteRequest
    ): Response<Void>

    @GET("/post")
    suspend fun getPostList(
        @Header("Authorization") header: String,
        @Query("page") page: Int,
        @Query("feel") feel: String
    ): Response<PostListResponse>

    @GET("/comment/{post_id}")
    suspend fun getComment(
        @Header("Authorization") header: String,
        @Path("post_id") post_id: String,
        @Query("page") page: Int
    ): Response<CommentContentResponseList>

    @POST("/comment/{post_id}")
    suspend fun writeComment(
        @Header("Authorization") header: String,
        @Path("post_id") post_id: String,
        @Body body: String
    ) : Response<Void>

    @DELETE("/comment/{comment_id}")
    suspend fun deleteComment(
        @Header("Authorization") header: String,
        @Path("comment_id") comment_id: Int
    ): Response<Void>

    @PUT("/like")
    suspend fun like(
        @Header("Authorization") header: String,
        @Query("post_id") post_id: String
    ): Response<Void>

    @POST("/report")
    suspend fun report(
        @Header("Authorization") header: String,
        @Body body: FeedReportRequest,
        @Query("id") id: String,
        @Query("type") type: String
    ): Response<Void>

    @GET("/profile")
    suspend fun profile(
        @Header("Authorization") header: String,
        @Query("id") id: String,
        @Query("feel") feel: String,
        @Query("filter") filter: String,
        @Query("page") page: Int
    ): Response<ProfileResponse>
}