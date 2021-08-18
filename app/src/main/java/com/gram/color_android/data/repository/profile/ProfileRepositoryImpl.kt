package com.gram.color_android.data.repository.profile

import com.gram.color_android.data.model.feed.ProfileResponse
import com.gram.color_android.network.RetrofitClient
import com.gram.color_android.network.SafeApiRequest
import retrofit2.Response

class ProfileRepositoryImpl : ProfileRepository, SafeApiRequest() {
    override suspend fun profile(
        header: String,
        id: String,
        feel: String,
        filter: String,
        page: Int
    ): Response<ProfileResponse> {
        return safeApiCall { RetrofitClient.getFastAPI().profile(header, id, feel, filter, page) }
    }
}