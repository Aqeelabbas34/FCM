package com.aqeel.adminapp.notifications

import android.app.DownloadManager.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject

class FCM {
    private val client = OkHttpClient()
    private val url = "https://fcm.googleapis.com/v1/projects/notifications-c1775/messages:send"

    fun sendFCMNotification(targetDeviceToken: String, title: String, body: String, accessToken: String) {
        // Use a coroutine to send the notification in a background thread
        CoroutineScope(Dispatchers.IO).launch {
            sendNotificationTask(targetDeviceToken, title, body, accessToken)
        }
    }

    private fun sendNotificationTask(targetDeviceToken: String, title: String, body: String, accessToken: String) {
        try {
            // Build the JSON payload
            val json = JSONObject()
            val message = JSONObject()
            val data = JSONObject()

            data.put("title", title)
            data.put("body", body)
            message.put("token", targetDeviceToken)
            message.put("data", data)
            json.put("message", message)

            // Create the request body
            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            // Build the request
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $accessToken")
                .post(requestBody)
                .build()

            // Execute the request
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Failed to send notification: ${response.code} ${response.message}")
                } else {
                    println("Notification sent successfully")
                }
            }
        } catch (e: Exception) {
            println("Error sending notification: ${e.message}")
        }
    }

}