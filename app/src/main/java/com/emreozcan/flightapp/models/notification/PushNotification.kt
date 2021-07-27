package com.emreozcan.flightapp.models.notification

data class PushNotification(
    val data: NotificationData,
    val to: String
)