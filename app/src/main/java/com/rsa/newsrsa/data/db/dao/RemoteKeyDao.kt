package com.rsa.newsrsa.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rsa.newsrsa.api.response_handler.ArticleRemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKey: List<ArticleRemoteKey>)

    @Query("DELETE FROM ArticleRemoteKey")
    suspend fun deleteArticleRemoteKey()

    @Query("DELETE FROM ArticleRemoteKey WHERE id =:id")
    suspend fun deleteArticleRemoteKey(id : String)

    @Query("SELECT * FROM ArticleRemoteKey where id=:id")
    suspend fun getRemoteKey(id: String): ArticleRemoteKey
}