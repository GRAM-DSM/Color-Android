package com.gram.color_android.data.repository.write

import com.gram.color_android.data.model.write.WriteRequest
import com.gram.color_android.network.RetrofitClient
import com.gram.color_android.network.SafeApiRequest
import retrofit2.Response

class WriteRepositoryImpl : WriteRepository, SafeApiRequest() {
    override suspend fun write(header: String, body: WriteRequest): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().write(header, body) }
    }

    override suspend fun updatePost(
        header: String,
        post_id: String,
        body: WriteRequest
    ): Response<Void> {
        return safeApiCall { RetrofitClient.getSpringAPI().updatePost(header, post_id, body) }
    }
}