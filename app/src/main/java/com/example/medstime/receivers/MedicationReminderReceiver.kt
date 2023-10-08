package com.example.medstime.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.ReminderModel
import com.example.domain.usecase.common.ChangeNotificationStatus
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.medstime.R
import com.example.medstime.services.BannerDisplayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.Calendar


class MedicationReminderReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "MedicationReminderReceiver"
    }

    private val getMedicationIntakeModel: GetMedicationIntakeModel by KoinJavaComponent.inject(
        GetMedicationIntakeModel::class.java
    )
    private val changeNotificationStatus: ChangeNotificationStatus by KoinJavaComponent.inject(
        ChangeNotificationStatus::class.java
    )

    override fun onReceive(context: Context?, intent: Intent?) {
        val medicationIntakeId = intent?.getStringExtra("intakeModelId")!!//todo
        val reminderModelId = intent.getStringExtra("reminderModelId")!!//todo
        val type = intent.getStringExtra("type")!!
        CoroutineScope(Dispatchers.IO).launch {
            changeNotificationStatus.invoke(reminderModelId, ReminderModel.Status.SHOWN)
        }
        when (type) {
            ReminderModel.Type.PUSH_NOTIFICATION.toString() -> sendNotification(
                context = context!!,
                medicationIntakeId = medicationIntakeId
            )

            ReminderModel.Type.BANNER.toString() -> startBannerService(
                context = context!!,
                medicationIntakeModelId = medicationIntakeId,
                reminderModelId = reminderModelId,
            )
        }
    }

    private fun startBannerService(
        context: Context,
        medicationIntakeModelId: String,
        reminderModelId: String
    ) {
        val serviceIntent = Intent(context, BannerDisplayService::class.java)
        serviceIntent.putExtra("intakeModelId", medicationIntakeModelId)
        serviceIntent.putExtra("reminderModelId", reminderModelId)
        context.startForegroundService(serviceIntent)
    }

    private fun sendNotification(context: Context, medicationIntakeId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val intake = getMedicationIntakeModel.invoke(medicationIntakeId)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "medication_channel",
                "Напоминания о приеме лекарств",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(context, "medication_channel")
                .setSmallIcon(R.drawable.menu_icon_medication)
                .setContentTitle("Напоминание о приеме лекарства ${intake.name}")
                .setContentText("Прием назначен на ${intake.intakeTime.toDisplayString()}")
                .addAction(
                    R.drawable.button_icon_check,
                    context.getString(R.string.taken),
                    getPendingIntent(context, true, medicationIntakeId)
                )
                .addAction(
                    R.drawable.button_icon_skip,
                    context.getString(R.string.skipped),
                    getPendingIntent(context, false, medicationIntakeId)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(medicationIntakeId.hashCode(), builder.build())
        }
    }

    private fun MedicationIntakeModel.Time.toDisplayString() =
        if (minute < 10) "${hour}:0${minute}"
        else "${hour}:${minute}"


    private fun getPendingIntent(
        context: Context?,
        isTaken: Boolean,
        medicationIntakeId: String
    ): PendingIntent {
        val intent = Intent(context, ChangeIsTakenStatusReceiver::class.java)
        intent.apply {
            val actualTime = getActualTime()
            putExtra("isTaken", isTaken)
            putExtra("medicationIntakeId", medicationIntakeId)
            putExtra("actualIntakeHour", actualTime.hour)
            putExtra("actualIntakeMinute", actualTime.minute)
        }
        Log.d(TAG, medicationIntakeId.hashCode().toString())
        return PendingIntent.getBroadcast(
            context,
            medicationIntakeId.hashCode(),//TODO
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getActualTime(): MedicationIntakeModel.Time {
        val calendar = Calendar.getInstance()
        return MedicationIntakeModel.Time(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }
}



