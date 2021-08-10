package com.gram.color_android.data.repository.write

import com.gram.color_android.data.model.WriteRequest
import retrofit2.Response

interface WriteRepository {
    suspend fun write(header: String, body: WriteRequest) : Response<Void>
}