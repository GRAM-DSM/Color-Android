package com.gram.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.gram.color_android.data.model.feed.PostListResponse
import com.gram.color_android.data.repository.feed.FeedRepositoryImpl
import com.gram.color_android.network.set.FeedSet
import kotlinx.coroutines.launch
import retrofit2.Response

class FeedViewModel : ViewModel() {
    private val feedRepository = FeedRepositoryImpl()
    private val _feedLiveData: MutableLiveData<FeedSet> = MutableLiveData()
    private val _feedListLiveData: MutableLiveData<PostListResponse> = MutableLiveData()
    val feedLiveData = _feedLiveData
    val feedListLiveData = _feedListLiveData

    fun getPostList(header: String, page: Int, feel: String) {
        viewModelScope.launch {
            val response = feedRepository.getPostList(header, page, feel )
            if(response.isSuccessful){
                getPostSuccess(response)
            } else{
                _feedLiveData.postValue(FeedSet.GET_FAIL)
            }
        }
    }

    private fun getPostSuccess(response : Response<PostListResponse>){
        if(response.code() == 200){
            _feedListLiveData.postValue(response.body())
            _feedLiveData.postValue(FeedSet.GET_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.GET_FAIL)
        }
    }
}