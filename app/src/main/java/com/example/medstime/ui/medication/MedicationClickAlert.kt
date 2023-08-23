package com.example.medstime.ui.medication

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.medstime.databinding.MedicationAlertDialogBinding
import com.example.medstime.domain.models.MedicationIntakeModel


class MedicationClickAlert(
    context: Context,
    medicationIntakeModel: MedicationIntakeModel,
    timeAndDosageText: String,
) : AlertDialog(context) {
    init {
        val inflater = LayoutInflater.from(context)
        val binding = MedicationAlertDialogBinding.inflate(inflater)
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        with(binding) {
            ADMedicationName.text = medicationIntakeModel.name
            ADTimeAndDosage.text = timeAndDosageText
            ADEditButton.setOnClickListener {
                dismiss()
            }
            ADRemindFiveMinButton.setOnClickListener {
                dismiss()
            }
            ADSkipButton.setOnClickListener {
                dismiss()
            }
            ADTakenButton.setOnClickListener {
                dismiss()
            }
        }
    }
}