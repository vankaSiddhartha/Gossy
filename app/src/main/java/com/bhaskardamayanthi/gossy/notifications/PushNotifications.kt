package com.bhaskardamayanthi.gossy.notifications


data class PushNotifications(
    val data: NotificationData,
    val to: String
)