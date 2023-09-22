package com.rsa.newsrsa.common

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesHelper @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        const val PREFERENCE_NAME = "com.rsa.newsrsa"
    }

    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)


    // Save strings in preference
    fun savePreferenceString(key: String?, value: String?) {
        val editor = mSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Save boolean values in preference
    fun savePreferencesBoolean(key: String?, value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    // Save int values in preference
    fun savePreferencesInt(key: String?, value: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    fun getPreferenceInt(key: String?): Int {
        return mSharedPreferences.getInt(key, 1)
    }
    // Get string values from preference
    fun getPreferenceString(key: String?): String? {
        return mSharedPreferences.getString(key, null)
    }

    // Get boolean values from preference
    fun getPreferencesBoolean(key: String?): Boolean {
        return mSharedPreferences.getBoolean(key, false) //false is default value
    }


    //clear the value
    fun clearTheValue() {
        val editor = mSharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


}