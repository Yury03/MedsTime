package com.example.medstime.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.medstime.R
import com.example.medstime.services.BannerDisplayService

class MedicationReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val medicationModelID = intent?.getStringExtra("medicationModelID")
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "medication_channel",
            "Medication Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, "medication_channel")
            .setSmallIcon(R.drawable.medication_icon_menu)
            .setContentTitle("Напоминание о приеме лекарства")
            .setContentText("Примите лекарство сейчас.")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)


        notificationManager.notify(1, builder.build())

        val serviceIntent = Intent(context, BannerDisplayService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

}


