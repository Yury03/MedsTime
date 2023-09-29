package com.example.medstime.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.domain.models.ReminderModel
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.services.BannerDisplayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class MedicationReminderReceiver : BroadcastReceiver() {
    private val getMedicationIntakeModel: GetMedicationIntakeModel by KoinJavaComponent.inject(
        GetMedicationIntakeModel::class.java
    )

    override fun onReceive(context: Context?, intent: Intent?) {
        val medicationModelId = intent?.getStringExtra("intakeModelId")
        val type = intent?.getStringExtra("type")!!
        when (type) {
            ReminderModel.Type.BANNER.toString() -> sendNotification(
                context = context!!,
                medicationModelID = medicationModelId!!
            )//todo
            ReminderModel.Type.PUSH_NOTIFICATION.toString() -> startBannerService(
                context = context!!,
                medicationIntakeModelId = medicationModelId!!
            )//todo
        }
    }

    private fun startBannerService(context: Context, medicationIntakeModelId: String) {
        val serviceIntent = Intent(context, BannerDisplayService::class.java)
        serviceIntent.putExtra("intakeModelId", medicationIntakeModelId)
        context.startService(serviceIntent)
    }

    private fun sendNotification(context: Context, medicationModelID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val intake = getMedicationIntakeModel.invoke(medicationModelID)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "medication_channel",
                "Medication Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(context, "medication_channel")
                .setSmallIcon(R.drawable.medication_icon_menu)
                .setContentTitle("Напоминание о приеме лекарства ${intake.name}")
                .setContentText("Прием назначен на ${intake.intakeTime.hour}:${intake.intakeTime.minute}")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }
    }

}


