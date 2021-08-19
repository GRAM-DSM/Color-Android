package com.gram.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.color_android.data.model.profile.ProfileResponse
import com.gram.color_android.data.repository.profile.ProfileRepositoryImpl
import com.gram.color_android.network.set.ProfileSet
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepositoryImpl()
    private val _profileLiveData : MutableLiveData<ProfileSet> = MutableLiveData()
    private val _profileListLiveData : MutableLiveData<ProfileResponse> = MutableLiveData()
    val profileLiveData = _profileLiveData
    val profileListLiveData = _profileListLiveData

    fun getProfilePost(header: String, id: String, feel: String, filter: String, page: Int){
        viewModelScope.launch {
            val response = profileRepository.profile(header, id, feel, filter, page)
            if(response.isSuccessful){
                getProfilePostSuccess(response)
            } else {
                _profileLiveData.postValue(ProfileSet.GET_FAIL)
            }
        }
    }

    private fun getProfilePostSuccess(response: Response<ProfileResponse>) {
        if(response.code() == 200){
            _profileLiveData.postValue(ProfileSet.GET_SUCCESS)
            _profileListLiveData.postValue(response.body())
        } else {
            _profileLiveData.postValue(ProfileSet.GET_FAIL)
        }
    }
}