package com.rsa.newsrsa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rsa.newsrsa.api.mangers.NewsApiManager
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.common.NetworkHelper
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.data.db.AppDataBase
import com.rsa.newsrsa.di.NewsRemoteMediator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NewsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val newsApiManager: NewsApiManager,
    private val appDataBase: AppDataBase,
    private val preferencesHelper: PreferencesHelper,
    private val networkHelper: NetworkHelper
) : NewsArticleRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getArticleList(country: String): LiveData<PagingData<Article>> {
        return Pager(config = PagingConfig(pageSize = 5, maxSize = 20),
            remoteMediator = NewsRemoteMediator(newsApiManager, appDataBase, preferencesHelper,country,networkHelper,context),
            pagingSourceFactory = {
                appDataBase.articleDao().fetchArticle(country)
            }).liveData
    }

    override suspend fun getArticleReadLaterList(): LiveData<PagingData<Article>> {
        return Pager(config = PagingConfig(pageSize = 5, maxSize = 20), pagingSourceFactory = {
            appDataBase.articleDao().fetchReadLaterArticle()
        }).liveData
    }

    override suspend fun getArticle(id: String): List<Article> {
        return appDataBase.articleDao().fetchArticleByID(id)
    }
}

interface NewsArticleRepository {
    suspend fun getArticleList(country: String): LiveData<PagingData<Article>>
    suspend fun getArticleReadLaterList(): LiveData<PagingData<Article>>
    suspend fun getArticle(id : String): List<Article>
}
