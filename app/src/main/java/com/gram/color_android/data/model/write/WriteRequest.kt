package com.gram.color_android.data.model.write

data class WriteRequest(
    val content: String,
    val feel: String,
    val hash_tag : List<String>
)
