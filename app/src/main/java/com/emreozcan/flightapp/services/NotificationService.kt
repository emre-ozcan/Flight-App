package com.emreozcan.flightapp.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.ui.LoginActivity
import com.emreozcan.flightapp.util.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.emreozcan.flightapp.util.createNotificationChannelForO


class NotificationService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper(context).createNotification()

    }
}

class NotificationHelper(val context: Context?) {

    fun createNotification() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val resultPendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_airplane)
            .setContentTitle(context.getString(R.string.daily_notification))
            .setContentText(context.getString(R.string.check_application))
            .setContentIntent(resultPendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannelForO(notificationManager,NOTIFICATION_CHANNEL_ID)
        }
        notificationManager.notify(0,notification.build())

    }
}
