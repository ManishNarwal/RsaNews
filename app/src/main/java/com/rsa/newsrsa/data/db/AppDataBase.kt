package com.rsa.newsrsa.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rsa.newsrsa.api.response_handler.ArticleRemoteKey
import com.rsa.newsrsa.api.response_handler.NewsData.Article
import com.rsa.newsrsa.data.db.dao.NewsDao
import com.rsa.newsrsa.data.db.dao.RemoteKeyDao
import com.rsa.newsrsa.data.db.typeconverter.DateConverter
import com.rsa.newsrsa.data.db.typeconverter.StringTypeConverter
import net.sqlcipher.database.SupportFactory

private const val PASSCODE = "SWEN1PASS"
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
                .openHelperFactory(SupportFactory(PASSCODE.toByteArray()))
                .fallbackToDestructiveMigration().build()
    }
}