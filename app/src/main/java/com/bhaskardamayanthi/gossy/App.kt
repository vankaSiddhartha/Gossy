package com.bhaskardamayanthi.gossy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bhaskardamayanthi.gossy.localStore.StoreManager
import com.google.firebase.messaging.FirebaseMessaging

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val storeManager =StoreManager(this)
        val getCollege = storeManager.getString("college","")
        val topic ="/topics/$getCollege"
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }
}