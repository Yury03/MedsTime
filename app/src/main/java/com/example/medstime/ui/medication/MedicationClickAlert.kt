package com.example.medstime.ui.medication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.medstime.R


class MedicationClickAlert(
    context: Context,
    medicationIntakeModelID: String,
    timeAndDosageText: String,
) : DialogFragment() {

    //    init {
//        val inflater = LayoutInflater.from(context)
//        val binding = MedicationAlertDialogBinding.inflate(inflater)
////        setView(binding.root)
////        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        with(binding) {
////            ADMedicationName.text = medicationIntakeModel.name
////            ADTimeAndDosage.text = timeAndDosageText
////            ADEditButton.setOnClickListener {
////                dismiss()
////            }
////            ADRemindFiveMinButton.setOnClickListener {
////                dismiss()
////            }
////            ADSkipButton.setOnClickListener {
////                dismiss()
////            }
////            ADTakenButton.setOnClickListener {
////                dismiss()
////            }
//        }
//}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.medication_alert_dialog, container, false)
    }
}