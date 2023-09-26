package com.rsa.newsrsa.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rsa.newsrsa.BuildConfig
import com.rsa.newsrsa.R
import com.rsa.newsrsa.databinding.FragmentSplashBinding
import com.rsa.newsrsa.ui.activity.MainActivity
import com.scottyab.rootbeer.RootBeer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var activity: MainActivity
    private lateinit var binding: FragmentSplashBinding
    private lateinit var rootBeer: RootBeer
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val bottomNav =
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNav.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            if (BuildConfig.DEBUG) {
                if (activity.fragmentStatus)
                    activity.navController.navigate(R.id.action_splashFragment_to_newsReadLaterFragment)
                else activity.navController.navigate(R.id.action_splashFragment_to_tabFragment)
            } else {
                rootBeer = RootBeer(activity)
                if (rootBeer.isRooted) {
                    showError()
                } else {
                    if (activity.fragmentStatus)
                        activity.navController.navigate(R.id.action_splashFragment_to_newsReadLaterFragment)
                    else activity.navController.navigate(R.id.action_splashFragment_to_tabFragment)
                }
            }
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        val bottomNav =
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNav.visibility = View.VISIBLE
    }

    private fun showError() {
        MaterialAlertDialogBuilder(activity).setTitle(R.string.error)
            .setMessage(R.string.rooted_error_msg).setPositiveButton(R.string.ok_text) { _, _ ->
                activity.finish()
            }.show()
    }
}