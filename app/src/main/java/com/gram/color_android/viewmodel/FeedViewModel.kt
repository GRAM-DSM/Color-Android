package com.gram.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.color_android.data.model.feed.CommentContentResponseList
import com.gram.color_android.data.model.feed.CommentRequest
import com.gram.color_android.data.model.feed.FeedReportRequest
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

    fun getPostMore(header: String, page: Int, feel: String) {
        viewModelScope.launch {
            val response = feedRepository.getPostList(header, page, feel)
            if(response.isSuccessful){
                getMoreSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.GET_MORE_FAIL)
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

    fun deleteComment(header: String, post_id: String, comment_id: String) {
        viewModelScope.launch {
            val response = feedRepository.deleteComment(header, post_id, comment_id)
            if(response.isSuccessful){
                deleteCommentSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.DELETE_COM_FAIL)
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

    fun writeComment(header: String, post_id: String, body: CommentRequest){
        viewModelScope.launch {
            val response = feedRepository.writeComment(header, post_id, body)
            if(response.isSuccessful){
                writeCommentSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.WRITE_FAIL)
            }
        }
    }

    fun report(header: String, body: FeedReportRequest, id: String, type: String){
        viewModelScope.launch {
            val response = feedRepository.report(header, body, id, type)
            if(response.isSuccessful){
                reportSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.REPORT_FAIL)
            }
        }
    }

    fun like(header: String, post_id: String){
        viewModelScope.launch {
            val response = feedRepository.like(header, post_id)
            if(response.isSuccessful){
                likeSuccess(response)
            } else {
                _feedLiveData.postValue(FeedSet.LIKE_FAIL)
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

    private fun getMoreSuccess(response: Response<PostListResponse>) {
        if(response.code() == 200){
            _feedListLiveData.value!!.postContentResponseList.addAll(response.body()!!.postContentResponseList)
            _feedLiveData.postValue(FeedSet.GET_MORE_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.GET_MORE_FAIL)
        }
    }


    private fun deletePostSuccess(response: Response<Void>) {
        if(response.code() == 200){
            _feedLiveData.postValue(FeedSet.DELETE_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.DELETE_FAIL)
        }
    }

    private fun deleteCommentSuccess(response: Response<Void>) {
        if(response.code() == 200 || response.code() == 204){
            _feedLiveData.postValue(FeedSet.DELETE_COM_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.DELETE_COM_FAIL)
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

    private fun writeCommentSuccess(response: Response<Void>){
        if(response.code() == 200){
            _feedLiveData.postValue(FeedSet.WRITE_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.WRITE_FAIL)
        }
    }

    private fun reportSuccess(response: Response<Void>) {
        if(response.code() == 200){
            _feedLiveData.postValue(FeedSet.REPORT_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.REPORT_FAIL)
        }
    }

    private fun likeSuccess(response: Response<Void>){
        if(response.code() == 200){
            _feedLiveData.postValue(FeedSet.LIKE_SUCCESS)
        } else {
            _feedLiveData.postValue(FeedSet.LIKE_FAIL)
        }
    }
}