package com.example.logistiq

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.libraries.places.api.Places

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")

            if (apiKey != null && apiKey.isNotEmpty() && apiKey != "YOUR_API_KEY") {
                Places.initialize(applicationContext, apiKey)
            } else {
                Log.e("MyApplication", "API Key for Google Places is missing or a placeholder.")
            }
        } catch (e: Exception) {
            Log.e("MyApplication", "Error initializing Google Places", e)
            // Prevent app crash
        }
    }
}