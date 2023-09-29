package com.example.medstime.services

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.medstime.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class BannerDisplayService : Service() {
    private val getMedicationIntakeModel: GetMedicationIntakeModel by inject()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reminderModelId = intent?.getStringExtra("intakeModelId")!!//todo
        CoroutineScope(Dispatchers.IO).launch {
            val intakeModel = getMedicationIntakeModel.invoke(reminderModelId)
            withContext(Dispatchers.Main) {
                showAlertDialog(intakeModel)
            }
        }
        return START_NOT_STICKY
    }

    private fun showAlertDialog(intake: MedicationIntakeModel) {
        Dialog(this, R.style.MedicationBanner).apply {
            setContentView(R.layout.medication_alert_dialog)
            window?.setBackgroundDrawableResource(R.color.local_transparent)
            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            val closeButton = findViewById<Button>(R.id.AD_skipButton)
            closeButton.setOnClickListener {
                this.dismiss()
            }
            val title = findViewById<TextView>(R.id.AD_medicationName)
            val timeAndDosage = findViewById<TextView>(R.id.AD_timeAndDosage)
            title.text = intake.name
            timeAndDosage.text = "${intake.intakeTime.hour}:${intake.intakeTime.minute} ${intake.dosage} ${intake.dosageUnit}"
            show()
        }
    }
}
