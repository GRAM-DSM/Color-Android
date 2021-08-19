package com.gram.color_android.data.repository.profile

import com.gram.color_android.data.model.profile.ProfileResponse
import retrofit2.Response

interface ProfileRepository {
    suspend fun profile(header: String, id: String, feel: String, filter: String, page: Int): Response<ProfileResponse>
}