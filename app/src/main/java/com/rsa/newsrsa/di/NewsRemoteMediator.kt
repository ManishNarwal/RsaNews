package com.rsa.newsrsa.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rsa.newsrsa.BuildConfig
import com.rsa.newsrsa.api.mangers.NewsApiManager
import com.rsa.newsrsa.api.response_handler.ArticleRemoteKey
import com.rsa.newsrsa.common.NetworkHelper
import com.rsa.newsrsa.api.response_handler.NewsData.Article as Article
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.data.db.AppDataBase
import com.rsa.newsrsa.utils.showToast
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val networkManager: NewsApiManager,
    private val appDataBase: AppDataBase,
    private val preferencesHelper: PreferencesHelper,
    private val country: String,
    private val networkHelper: NetworkHelper,
    private val context: Context
) : RemoteMediator<Int, Article>() {
    private val newsDao = appDataBase.articleDao()
    private val remoteKeyDao = appDataBase.articleRemoteKeyDao()
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKey?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKey?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                    nextPage
                }
            }
            val articleList = mutableListOf<Article>()
            var isEndPageReached = false
            if (networkHelper.isNetworkConnected()) {
                val response = networkManager.getNewsArticles(
                    BuildConfig.API_KEY,
                    country = country,
                    currentPage,
                    5
                )
                articleList.addAll(response.body()?.articles!!)
                isEndPageReached = response.body()?.totalResults == currentPage
            }else{
                context.showToast("No Internet! Please check")
            }
            val countryCode = preferencesHelper.getPreferenceString("country")
            var count = preferencesHelper.getPreferenceInt("count")
            articleList.map {
                it.country = countryCode
                it.id = "$countryCode${count++}"
                preferencesHelper.savePreferencesInt("count", count)
            }
            Timber.e("Updated Article List : $articleList")

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (isEndPageReached) null else currentPage + 1
            appDataBase.withTransaction {
                newsDao.insertArticleList(articleList)
                val keys = articleList.map { article ->
                    ArticleRemoteKey(
                        id = article.id, prevPage, nextPage
                    )
                }
                remoteKeyDao.addAllRemoteKeys(keys)
            }
            MediatorResult.Success(isEndPageReached)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): ArticleRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKey(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Article>
    ): ArticleRemoteKey? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            remoteKeyDao.getRemoteKey(id = it.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Article>
    ): ArticleRemoteKey? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            remoteKeyDao.getRemoteKey(id = it.id)
        }
    }

}