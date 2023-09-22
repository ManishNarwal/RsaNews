package com.rsa.newsrsa.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsReadLaterViewModel @Inject constructor(
    private val networkRepository: NewsRepository,
) : ViewModel(), LifecycleObserver {
    private val _dataList = MutableLiveData<PagingData<Article>>()
    suspend fun getArticleReadLaterList(): LiveData<PagingData<Article>> {
        val response = networkRepository.getArticleReadLaterList().cachedIn(viewModelScope)
        _dataList.value = response.value
        return response
    }
}