package com.aqeel.adminapp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aqeel.adminapp.notifications.AccessToken
import com.aqeel.adminapp.notifications.FCM
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var notificationBtn = findViewById<Button>(R.id.notificationBtn)
        notificationBtn.setOnClickListener{
            val fcm = FCM()
            val title="FCM"
            val body ="Notification from admin"
            AccessToken.getAccessTokenAsync(object : AccessToken.AccessTokenCallback{
                override fun onAccessTokenReceived(token: String?) {
                  if (token!=null){
                      val db= FirebaseFirestore.getInstance()
                      db.collection("User")
                          .get()
                          .addOnCompleteListener{task->
                              if (task.isSuccessful){
                                  val snapshot= task.result
                                  if (snapshot!=null){
                                      for (document in snapshot){
                                          val deviceToken= document.getString("deviceToken")
                                          if (deviceToken!=null){
                                              fcm.sendFCMNotification(deviceToken,title,body,token)
                                          }
                                      }
                                  }
                              }
                          }
                  }
                }

            })


        }
    }
}