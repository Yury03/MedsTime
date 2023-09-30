package com.example.medstime.services

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.usecase.reminder.ChangeMedicationIntakeIsTaken
import com.example.domain.usecase.reminder.GetMedicationIntakeModel
import com.example.medstime.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.Calendar


class BannerDisplayService : Service() {
    private val getMedicationIntakeModel: GetMedicationIntakeModel by inject()
    private val changeIsTakenStatus: ChangeMedicationIntakeIsTaken by inject()
    private val coroutineDispatcher: CoroutineDispatcher by inject()
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
        val scope = CoroutineScope(coroutineDispatcher)
        Dialog(this, R.style.MedicationBanner).apply {
            setContentView(R.layout.medication_alert_dialog)
            window?.setBackgroundDrawableResource(R.color.local_transparent)
            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            val skip = findViewById<Button>(R.id.AD_skipButton)
            skip.setOnClickListener {
                scope.launch {
                    changeIsTakenStatus.invoke(intake.id, false, null)
                }
                dismiss()
            }
            val taken = findViewById<Button>(R.id.AD_takenButton)
            taken.setOnClickListener {
                scope.launch {
                    changeIsTakenStatus.invoke(intake.id, true, getActualTime())
                }
                dismiss()
            }
            val title = findViewById<TextView>(R.id.AD_medicationName)
            val timeAndDosage = findViewById<TextView>(R.id.AD_timeAndDosage)
            title.text = intake.name
            timeAndDosage.text = buildTimeAndDosageText(intake)
            show()
        }
    }

    private fun getActualTime(): MedicationIntakeModel.Time {
        val calendar = Calendar.getInstance()
        return MedicationIntakeModel.Time(
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE)
        )
    }

    private fun buildTimeAndDosageText(medicationIntakeModel: MedicationIntakeModel): String {
        val time = buildTimeString(medicationIntakeModel.intakeTime)
        val dosage = buildDosageString(medicationIntakeModel)
        return "$time $dosage"
    }

    private fun buildTimeString(time: MedicationIntakeModel.Time): String =
        with(time) {
            val minuteString =
                if (minute < 10) {
                    "0$minute"
                } else {
                    "$minute"
                }
            val hourString = if (hour < 10) {
                "0$hour"
            } else {
                "$hour"
            }
            return "$hourString:$minuteString"
        }

    private fun buildDosageString(medicationIntakeModel: MedicationIntakeModel): String {
        val commonText =
            "${medicationIntakeModel.dosageUnit} ${medicationIntakeModel.intakeType.toRussianString()}"
        return if (medicationIntakeModel.dosage == medicationIntakeModel.dosage.toInt().toDouble())
            "${medicationIntakeModel.dosage.toInt()} $commonText"
        else
            "${medicationIntakeModel.dosage} $commonText"
    }

    private fun MedicationIntakeModel.IntakeType.toRussianString() =
        when (this) {
            MedicationIntakeModel.IntakeType.AFTER_MEAL -> getString(R.string.after_meal)
            MedicationIntakeModel.IntakeType.BEFORE_MEAL -> getString(R.string.before_meal)
            MedicationIntakeModel.IntakeType.DURING_MEAL -> getString(R.string.during_meal)
            MedicationIntakeModel.IntakeType.NONE -> getString(R.string.empty_string)
        }
}
