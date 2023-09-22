package com.rsa.newsrsa.api.mangers

import com.rsa.newsrsa.api.apicall.ApiService
import com.rsa.newsrsa.api.response_handler.NewsData
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NewsApiManager @Inject constructor(private val apiService: ApiService) {

    suspend fun getNewsArticles(apiKey: String,
                             country : String,
                             page : Int,
                             pageSize : Int
                             ): Response<NewsData> {
        return apiService.getNews(apiKey,country,"en", page, pageSize)
    }
}