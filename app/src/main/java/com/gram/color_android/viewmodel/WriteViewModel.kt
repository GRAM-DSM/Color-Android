package com.gram.color_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gram.color_android.data.model.WriteRequest
import com.gram.color_android.data.repository.write.WriteRepositoryImpl
import com.gram.color_android.network.set.WriteSet
import kotlinx.coroutines.launch
import retrofit2.Response

class WriteViewModel : ViewModel() {
    private val writeRepository = WriteRepositoryImpl()
    private val _writeLiveData : MutableLiveData<WriteSet> = MutableLiveData()
    val writeLiveData = _writeLiveData

    fun write(header : String, body : WriteRequest){
        viewModelScope.launch {
            val response = writeRepository.write(header, body)
            if(response.isSuccessful){
                writeSuccess(response)
            }
            else{
                _writeLiveData.postValue(WriteSet.WRITE_FAIL)
            }
        }
    }

    private fun writeSuccess(response : Response<Void>){
        if (response.code() == 201){
            _writeLiveData.postValue(WriteSet.WRITE_SUCCESS)
        } else {
            _writeLiveData.postValue(WriteSet.WRITE_FAIL)
        }
    }
}