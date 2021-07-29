package com.emreozcan.flightapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannelForO(notificationManager: NotificationManager,channelId: String) {
    val channelName = "Flight App"
    val channel = NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
        enableVibration(true)
        enableLights(true)
        description = "Flight Application"
    }

    notificationManager.createNotificationChannel(channel)
}


fun <T : RecyclerView.ViewHolder?> RecyclerView.setupRecyclerView(
    adapter: RecyclerView.Adapter<T>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
        this.context
    )
) {
    this.adapter = adapter
    this.layoutManager = layoutManager
}
fun Fragment.findNavControllerSafely(): NavController? {
    return if (isAdded) {
        findNavController()
    } else {
        null
    }
}



