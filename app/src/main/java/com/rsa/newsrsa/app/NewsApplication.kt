package com.rsa.newsrsa.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.data.db.AppDataBase
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.utils.NewsWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class NewsApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun onTerminate() {
        super.onTerminate()
        preferencesHelper.savePreferencesBoolean("NewsReadLaterFragment",false)
    }
    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(customWorkerFactory).build()

}

class CustomWorkerFactory @Inject constructor(private val appDataBase: AppDataBase) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context, workerClassName: String, workerParameters: WorkerParameters
    ): ListenableWorker = NewsWorker(appDataBase, appContext, workerParameters)
}