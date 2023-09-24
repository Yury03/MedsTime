package com.example.medstime.services

import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.example.medstime.R


class BannerDisplayService : Service() {
    override fun onCreate() {
        super.onCreate()
        showAlertDialog()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun showAlertDialog() {
        Dialog(this, R.style.MedicationBanner).apply {
            setContentView(R.layout.medication_alert_dialog)
            window?.setBackgroundDrawableResource(R.color.local_transparent)
            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            show()
        }
    }
}
