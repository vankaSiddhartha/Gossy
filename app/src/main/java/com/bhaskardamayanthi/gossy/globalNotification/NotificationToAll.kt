package com.bhaskardamayanthi.gossy.globalNotification

import android.util.Log
import com.bhaskardamayanthi.gossy.notifications.NotificationData
import com.bhaskardamayanthi.gossy.notifications.PushNotifications
import com.bhaskardamayanthi.gossy.notifications.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationToAll {
    fun sendNotificationToAll(title:String,message:String,token:String){

        PushNotifications(
            NotificationData(title, message),
            token
        ).also {
            sendNotification(it,token)
        }
    }
    private fun sendNotification(notification: PushNotifications,token: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
              //  FirebaseMessaging.getInstance().unsubscribeFromTopic(token)
            } else {
                Log.e("aaaaaa", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("aaaaaa", e.toString())
        }
    }
}