package com.emreozcan.flightapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.ui.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MessagingService : FirebaseMessagingService() {
    val channelId = "firebase_messaging"
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        handleNotification(p0)
    }

    private fun handleNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this,LoginActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = Random().nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannelForO(notificationManager)
        }

        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["message"])
            .setSmallIcon(R.drawable.ic_airplane)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelForO(notificationManager: NotificationManager) {
        val channelName = "Flight App"
        val channel = NotificationChannel(channelId,channelName,IMPORTANCE_HIGH).apply {
            enableVibration(true)
            enableLights(true)
            description = "Flight Application"
        }

        notificationManager.createNotificationChannel(channel)
    }
}