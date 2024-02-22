package com.example.medstime.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.domain.models.ReminderModel
import com.example.domain.usecase.reminder.GetRemindersWithStatus
import com.example.medstime.receivers.MedicationReminderReceiver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class ReminderService : Service() {

    private val getRemindersWithStatus: GetRemindersWithStatus by inject()
    private val coroutineDispatcher: CoroutineDispatcher by inject()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val scope = CoroutineScope(coroutineDispatcher)
        scope.launch {
            val reminders = getRemindersWithStatus(ReminderModel.Status.NONE)
            Log.d(TAG, reminders.toString())
            generateAlarms(reminders)
        }
        return START_NOT_STICKY
    }

    private fun generateAlarms(reminders: List<ReminderModel>) {
        val alarmManager = ContextCompat.getSystemService(
            this,
            AlarmManager::class.java
        ) as AlarmManager
        for (reminder in reminders) {
            val intent = Intent(this, MedicationReminderReceiver::class.java)
            intent.putExtra("intakeModelId", reminder.medicationIntakeId)
            intent.putExtra("reminderModelId", reminder.id)
            intent.putExtra("type", reminder.type.toString())
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                reminder.hashCode(),
                intent,
                PendingIntent.FLAG_MUTABLE
            )
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.timeShow, pendingIntent)
        }
    }

    companion object {

        private const val TAG = "ReminderService"
    }
}