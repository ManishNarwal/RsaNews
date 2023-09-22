package com.rsa.newsrsa.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rsa.newsrsa.R
import com.rsa.newsrsa.data.db.AppDataBase
import com.rsa.newsrsa.data.db.dao.NewsDao
import com.rsa.newsrsa.data.repository.NewsLocalRepository
import com.rsa.newsrsa.ui.activity.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
class NewsWorker @AssistedInject constructor(
    @Assisted private val appDataBase: AppDataBase,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    private val NOTIFICATION_ID = "NewNotificationID"
    private val NOTIFICATION_CHANNEL = "News_channel"
    private val NOTIFICATION_NAME = appContext.resources.getString(R.string.app_name)
    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        try {
            val newsDao = appDataBase.articleDao()
            val result = newsDao.fetchReadLaterArticleList()
            if (result.isNotEmpty()) {
                showNotification()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("readLaterFragment","NewsReadLaterFragment")
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.mipmap.news_icon)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_content))
            .setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1234, notification.build())
    }
}