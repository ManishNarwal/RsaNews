package com.rsa.newsrsa.di

import android.content.Context
import com.rsa.newsrsa.api.response_handler.ArticleRemoteKey
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.data.db.AppDataBase
import com.rsa.newsrsa.data.db.dao.NewsDao
import com.rsa.newsrsa.data.db.dao.RemoteKeyDao
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.utils.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) = context

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = AppDataBase.getDatabase(context)


    @Provides
    fun provideArticleDao(db: AppDataBase) = db.articleDao()

    @Provides
    fun provideArticleRemoteKeyDao(db: AppDataBase) = db.articleRemoteKeyDao()


    @Provides
    fun provideRepository(articleDao: NewsDao, remoteKeyDao: RemoteKeyDao) =
        NewsLocalRepository(articleDao, remoteKeyDao)


    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context) = ResourcesProvider(context)


    @Provides
    fun providePreferenceHelper(@ApplicationContext context: Context) = PreferencesHelper(context)
}