package com.example.medstime.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.usecase.medication_intake.ChangeMedicationIntakeIsTaken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class ChangeIsTakenStatusReceiver : BroadcastReceiver() {
    private val changeMedicationIntakeIsTaken: ChangeMedicationIntakeIsTaken by KoinJavaComponent.inject(
        ChangeMedicationIntakeIsTaken::class.java
    )

    override fun onReceive(context: Context?, intent: Intent?) {
        val isTaken = intent?.getBooleanExtra("isTaken", false)!!
        val medicationIntakeId = intent.getStringExtra("medicationIntakeId")!!
        val actualIntakeHour = intent.getIntExtra("actualIntakeHour", 0)
        val actualIntakeMinute = intent.getIntExtra("actualIntakeMinute", 0)
        val actualIntakeTime = if (isTaken) {
            MedicationIntakeModel.Time(actualIntakeHour, actualIntakeMinute)
        } else {
            null
        }
        CoroutineScope(Dispatchers.IO).launch {
            changeMedicationIntakeIsTaken.invoke(
                medicationIntakeId = medicationIntakeId,
                newIsTaken = isTaken,
                actualIntakeTime = actualIntakeTime,
            )
        }
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(medicationIntakeId.hashCode())
    }
}