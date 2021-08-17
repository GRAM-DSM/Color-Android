package com.gram.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.color_android.data.model.CommentContentResponseList
import com.gram.color_android.data.model.feed.PostListResponse
import com.gram.color_android.data.repository.feed.FeedRepositoryImpl
import com.gram.color_android.network.set.FeedSet
import kotlinx.coroutines.launch
import retrofit2.Response

class FeedViewModel : ViewModel() {
    private val feedRepository = FeedRepositoryImpl()
    private val _feedLiveData: MutableLiveData<FeedSet> = MutableLiveData()
    private val _feedListLiveData: MutableLiveData<PostListResponse> = MutableLiveData()
    private val _commentListLiveData: MutableLiveData<CommentContentResponseList> = MutableLiveData()
    val feedLiveData = _feedLiveData
    val feedListLiveData = _feedListLiveData
    val commentListLiveData = _commentListLiveData

    fun getPostList(header: String, page: Int, feel: String) {
        viewModelScope.launch {
            val response = feedRepository.getPostList(header, page, feel )
            if(response.isSuccessful){
                getPostSuccess(response)
            } else{
                _feedLiveData.postValue(FeedSet.GET_FEED_FAIL)
            }
        }
    }

    fun deletePost(header: String, post_id: String) {
        viewModelScope.launch {
            val response = feedRepository.deletePost(header, post_id)
            if(response.isSuccessful){
                deletePostSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.DELETE_FAIL)
            }
        }
    }

    fun getCommentList(header: String, post_id: String, page: Int){
        viewModelScope.launch {
            val response = feedRepository.getCommentList(header, post_id, page)
            if(response.isSuccessful){
                getCommentSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.GET_COMMENT_FAIL)
            }
        }
    }

    private fun getPostSuccess(response : Response<PostListResponse>){
        if(response.code() == 200){
            _feedListLiveData.postValue(response.body())
            _feedLiveData.postValue(FeedSet.GET_FEED_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.GET_FEED_FAIL)
        }
    }

    private fun deletePostSuccess(response: Response<Void>) {
        if(response.code() == 200){
            _feedLiveData.postValue(FeedSet.DELETE_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.DELETE_FAIL)
        }
    }

    private fun getCommentSuccess(response: Response<CommentContentResponseList>){
        if(response.code() == 200){
            _commentListLiveData.postValue(response.body())
            _feedLiveData.postValue(FeedSet.GET_COMMENT_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.GET_COMMENT_FAIL)
        }
    }
}