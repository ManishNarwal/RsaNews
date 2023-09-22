package com.rsa.newsrsa.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rsa.newsrsa.api.response_handler.ArticleRemoteKey
import com.rsa.newsrsa.api.response_handler.NewsData.Article as Article
import com.rsa.newsrsa.data.db.dao.NewsDao
import com.rsa.newsrsa.data.db.dao.RemoteKeyDao
import com.rsa.newsrsa.data.db.typeconverter.DateConverter
import com.rsa.newsrsa.data.db.typeconverter.StringTypeConverter

@Database(
    entities = [Article::class,ArticleRemoteKey::class], version = 1, exportSchema = false
)
@TypeConverters(StringTypeConverter::class, DateConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun articleDao(): NewsDao
    abstract fun articleRemoteKeyDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDataBase::class.java, "NewsArticleDB")
                .fallbackToDestructiveMigration().build()
    }
}