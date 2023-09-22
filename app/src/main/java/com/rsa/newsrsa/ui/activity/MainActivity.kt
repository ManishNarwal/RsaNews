package com.rsa.newsrsa.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rsa.newsrsa.R
import com.rsa.newsrsa.common.PreferencesHelper
import com.rsa.newsrsa.databinding.ActivityMainBinding
import com.rsa.newsrsa.ui.splash.SplashFragmentDirections
import com.rsa.newsrsa.utils.NewsWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var fragmentStatus : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(binding.toolbar)
        binding.bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.tabFragment,
                R.id.newsReadLaterFragment,
                R.id.notificationFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        scheduleNotificationWork()
        val fragmentTag = intent.getStringExtra("readLaterFragment")
        fragmentStatus = fragmentTag.equals("NewsReadLaterFragment")
    }

    private fun scheduleNotificationWork() {
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<NewsWorker>(
            5, TimeUnit.SECONDS
        ).build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}