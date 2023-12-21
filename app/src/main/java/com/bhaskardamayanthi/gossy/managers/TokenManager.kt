package com.bhaskardamayanthi.gossy.managers

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessaging

class TokenManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "FCMTokenPrefs"
        private const val FCM_TOKEN_KEY = "fcmToken"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveTokenLocally() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result // FCM token
                    saveToken(token)
                } else {
                    // Handle error getting FCM token
                }
            }
    }

     fun saveToken(token: String?) {
        token?.let {
            with(sharedPreferences.edit()) {
                putString(FCM_TOKEN_KEY, it)
                apply()
            }
        }
    }

    fun getSavedToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN_KEY, null)
    }
}
