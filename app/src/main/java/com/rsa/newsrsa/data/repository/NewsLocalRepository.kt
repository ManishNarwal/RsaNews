package com.rsa.newsrsa.data.repository

import com.rsa.newsrsa.api.response_handler.NewsData
import com.rsa.newsrsa.data.db.dao.NewsDao
import com.rsa.newsrsa.data.db.dao.RemoteKeyDao
import javax.inject.Inject

class NewsLocalRepository @Inject constructor(
    private val newsDao: NewsDao?,
    private val remoteKeyDao: RemoteKeyDao?,
) {
    suspend fun insertNews(news: NewsData.Article) {
        newsDao!!.insertArticle(news)
    }
    suspend fun deleteNewsArticle() {
        newsDao!!.deleteArticle()
    }
    suspend fun fetchReadLaterArticleList(): List<NewsData.Article> {
        return newsDao!!.fetchReadLaterArticleList()
    }
    suspend fun deleteNewsArticle(id : String) {
        newsDao!!.deleteArticle(id)
    }
    suspend fun deleteArticleRemoteKey(id : String) {
        remoteKeyDao!!.deleteArticleRemoteKey(id)
    }
    suspend fun isReadArticle(value : Boolean,id : String){
        newsDao!!.isReadArticle(value,id)
    }
    suspend fun isArticleMarkRead(id : String) : Boolean{
       return newsDao!!.isArticleMarkRead(id)
    }
    suspend fun isArticleMarkReadLater(id : String) : Boolean{
       return newsDao!!.isArticleMarkReadLater(id)
    }
    suspend fun readLaterArticle(value : Boolean,id : String){
        newsDao!!.readLaterArticle(value,id)
    }
}