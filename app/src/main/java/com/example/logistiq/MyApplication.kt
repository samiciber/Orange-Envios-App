package com.example.logistiq

import android.app.Application
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")

            if (apiKey != null && apiKey.isNotEmpty() && apiKey != "YOUR_API_KEY") {
                Places.initialize(applicationContext, apiKey)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}