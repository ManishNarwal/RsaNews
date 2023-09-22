package com.rsa.newsrsa.api.response_handler

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleRemoteKey(
    @PrimaryKey(autoGenerate = false) val id: String,
    val prevPage: Int?,
    val nextPage: Int?,
)
