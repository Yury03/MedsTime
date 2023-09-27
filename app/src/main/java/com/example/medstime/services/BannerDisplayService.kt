package com.example.medstime.services

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import android.widget.Button
import com.example.medstime.R


class BannerDisplayService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showAlertDialog()
        return START_NOT_STICKY
    }

    private fun showAlertDialog() {
        Dialog(this, R.style.MedicationBanner).apply {
            setContentView(R.layout.medication_alert_dialog)
            window?.setBackgroundDrawableResource(R.color.local_transparent)
            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            val closeButton = this.findViewById<Button>(R.id.AD_skipButton)
            closeButton.setOnClickListener {
                this.dismiss()
            }
            show()
        }
    }
}
