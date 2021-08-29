package com.gram.color_android.data.repository.profile

import com.gram.color_android.data.model.profile.ModifyNameRequest
import com.gram.color_android.data.model.profile.ProfileResponse
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

    override suspend fun modifyName(header: String, body: ModifyNameRequest): Response<Void> {
        return safeApiCall { RetrofitClient.getFastAPI().modifyName(header, body) }
    }
}