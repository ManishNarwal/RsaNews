package com.rsa.newsrsa.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsReadMoreViewModel @Inject constructor(
    private val networkRepository: NewsRepository,
) : ViewModel(), LifecycleObserver {
    suspend fun getArticle(id: String): List<Article> {
        return networkRepository.getArticle(id)
    }
}