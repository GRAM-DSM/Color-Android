package com.gram.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.color_android.data.model.profile.ModifyNameRequest
import com.gram.color_android.data.model.profile.ProfileResponse
import com.gram.color_android.data.repository.profile.ProfileRepositoryImpl
import com.gram.color_android.network.set.ProfileSet
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepositoryImpl()
    private val _profileLiveData : MutableLiveData<ProfileSet> = MutableLiveData()
    private val _postListLiveData : MutableLiveData<ProfileResponse> = MutableLiveData()
    val profileLiveData = _profileLiveData
    val postListLiveData = _postListLiveData

    fun getProfile(header: String, id: String, feel: String, filter: String, page: Int){
        viewModelScope.launch {
            val response = profileRepository.profile(header, id, feel, filter, page)
            if(response.isSuccessful){
                getProfileSuccess(response)
            } else {
                _profileLiveData.postValue(ProfileSet.GET_FAIL)
            }
        }
    }

    fun modifyName(header: String, body: ModifyNameRequest){
        viewModelScope.launch {
            val response = profileRepository.modifyName(header, body)
            if(response.isSuccessful){
                modifyNameSuccess(response)
            } else {
                profileLiveData.postValue(ProfileSet.MODIFY_FAIL)
            }
        }
    }

    private fun modifyNameSuccess(response: Response<Void>) {
        if(response.code() == 200){
            _profileLiveData.postValue(ProfileSet.MODIFY_SUCCESS)
        } else {
            _profileLiveData.postValue(ProfileSet.MODIFY_FAIL)
        }
    }

    private fun getProfileSuccess(response: Response<ProfileResponse>) {
        if(response.code() == 200){
            _postListLiveData.postValue(response.body())
            _profileLiveData.postValue(ProfileSet.GET_SUCCESS)
        } else {
            _profileLiveData.postValue(ProfileSet.GET_FAIL)
        }
    }
}