package com.rsa.newsrsa.api.apicall

import com.rsa.newsrsa.api.response_handler.NewsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
    ) : Response<NewsData>
}