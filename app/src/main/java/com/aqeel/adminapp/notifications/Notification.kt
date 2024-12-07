package com.aqeel.adminapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.aqeel.adminapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class Notification: FirebaseMessagingService() {
    private val channelId = "AssignId"

    override fun onCreate() {
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("NotificationService", "New Token: $token")
        // You can handle token storage or update logic here
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(manager)

        val title = message.data["title"] ?: "Notification"
        val body = message.data["body"] ?: ""

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(false)
            .build()

        val randomId = Random().nextInt()
        manager.notify(randomId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {
        val channel = NotificationChannel(
            channelId,
            "Assign Work",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "New work notifications"
            enableLights(true)
        }

        manager.createNotificationChannel(channel)
    }
}


