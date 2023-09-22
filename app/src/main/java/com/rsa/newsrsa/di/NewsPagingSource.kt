package com.rsa.newsrsa.di

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rsa.newsrsa.BuildConfig
import com.rsa.newsrsa.api.mangers.NewsApiManager
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val newsApiManager: NewsApiManager, private val country: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val apiQuery = country
        return try {
            val response = newsApiManager.getNewsArticles(
                BuildConfig.API_KEY, apiQuery,  position, params.loadSize
            )
            val repos = response.body()
            val nextKey = if (repos?.articles?.isEmpty()!!) {
                null
            } else {
//                position + (params.loadSize / PAGE_SIZE)
                position + 1
            }
            LoadResult.Page(
                data = repos.articles,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}