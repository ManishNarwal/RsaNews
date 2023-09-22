package com.rsa.newsrsa.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rsa.newsrsa.api.response_handler.NewsData.Article as Article
import java.util.*

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleList(articles: List<Article>)

    @Query("DELETE FROM Article")
    suspend fun deleteArticle()

    @Query("SELECT * FROM Article WHERE ReadLater = 0 AND Country=:country")
    fun fetchArticle(country: String): PagingSource<Int, Article>

    @Query("SELECT * FROM Article WHERE ReadLater=1")
    fun fetchReadLaterArticle(): PagingSource<Int, Article>

    @Query("SELECT * FROM Article WHERE id=:id")
    suspend fun fetchArticleByID(id : String): List<Article>

    @Query("SELECT * FROM Article WHERE ReadLater=1")
    fun fetchReadLaterArticleList(): List<Article>

    @Query("DELETE FROM Article WHERE id = :id")
    suspend fun deleteArticle(id: String): Int

    @Query("UPDATE Article SET ReadLater = :readLater WHERE id = :id")
    suspend fun readLaterArticle(readLater: Boolean, id: String): Int

    @Query("UPDATE Article SET IsRead = :isRead WHERE id = :id")
    suspend fun isReadArticle(isRead: Boolean, id: String): Int

    @Query("SELECT IsRead FROM Article WHERE id =:id")
    suspend fun isArticleMarkRead(id: String): Boolean

    @Query("SELECT ReadLater FROM Article WHERE id =:id")
    suspend fun isArticleMarkReadLater(id: String): Boolean

}