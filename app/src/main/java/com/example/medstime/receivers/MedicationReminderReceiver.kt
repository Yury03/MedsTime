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
import com.example.domain.usecase.medication_intake.GetMedicationIntakeModel
import com.example.domain.usecase.reminder.ChangeNotificationStatusByReminderId
import com.example.domain.usecase.reminder.GetReminderModelById
import com.example.medstime.R
import com.example.medstime.services.BannerDisplayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.Calendar


class MedicationReminderReceiver : BroadcastReceiver() {

    private val getMedicationIntakeModel: GetMedicationIntakeModel by KoinJavaComponent.inject(
        GetMedicationIntakeModel::class.java
    )
    private val changeNotificationStatus: ChangeNotificationStatusByReminderId by KoinJavaComponent.inject(
        ChangeNotificationStatusByReminderId::class.java
    )
    private val getReminderModelById: GetReminderModelById by KoinJavaComponent.inject(
        GetReminderModelById::class.java
    )

    override fun onReceive(context: Context?, intent: Intent?) {
        val medicationIntakeId = intent?.getStringExtra("intakeModelId")!!//todo
        val reminderModelId = intent.getStringExtra("reminderModelId")!!//todo
        val type = intent.getStringExtra("type")!!
        CoroutineScope(Dispatchers.IO).launch {
            val reminderModel = getReminderModelById(reminderModelId)
            if (reminderModel.status == ReminderModel.Status.NONE) {
                changeNotificationStatus(reminderModelId, ReminderModel.Status.SHOWN)
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
        }
    }

    private fun startBannerService(
        context: Context,
        medicationIntakeModelId: String,
        reminderModelId: String
    ) {
        val serviceIntent = Intent(context, BannerDisplayService::class.java)
        serviceIntent.putExtra("intakeModelId", medicationIntakeModelId)//todo string
        serviceIntent.putExtra("reminderModelId", reminderModelId)//todo string
        context.startService(serviceIntent)
    }

    private fun sendNotification(context: Context, medicationIntakeId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestCode = System.currentTimeMillis().toInt()
            val intake = getMedicationIntakeModel(medicationIntakeId)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "medication_channel",
                "Напоминания о приеме лекарств",//todo string
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(context, "medication_channel")
                .setSmallIcon(R.drawable.menu_icon_medication)
                .setContentTitle("Напоминание о приеме лекарства ${intake.name}")//todo string
                .setContentText("Прием назначен на ${intake.intakeTime.toDisplayString()}")//todo string
                .addAction(
                    R.drawable.button_icon_check,
                    context.getString(R.string.taken),
                    getPendingIntent(context, true, medicationIntakeId, requestCode)
                )
                .addAction(
                    R.drawable.button_icon_skip,
                    context.getString(R.string.skipped),
                    getPendingIntent(context, false, medicationIntakeId, requestCode + 1)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(medicationIntakeId.hashCode(), builder.build())
        }
    }

    private fun getPendingIntent(
        context: Context?,
        isTaken: Boolean,
        medicationIntakeId: String,
        requestCode: Int,
    ): PendingIntent {
        val intent = Intent(context, ChangeIsTakenStatusReceiver::class.java)
        Log.i(LOG_TAG, "$isTaken")
        intent.apply {
            val actualTime = getActualTime()
            putExtra("isTaken", isTaken)//todo string
            putExtra("medicationIntakeId", medicationIntakeId)//todo string
            putExtra("actualIntakeHour", actualTime.hour)//todo string
            putExtra("actualIntakeMinute", actualTime.minute)//todo string
        }
        Log.d(LOG_TAG, medicationIntakeId.hashCode().toString())
        return PendingIntent.getBroadcast(
            context,
            requestCode,
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

    companion object {

        private const val LOG_TAG = "MedicationReminderReceiver"
    }
}