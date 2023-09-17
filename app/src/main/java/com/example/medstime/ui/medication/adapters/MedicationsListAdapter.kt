package com.example.medstime.ui.medication.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medstime.R
import com.example.domain.models.MedicationIntakeModel
import java.time.LocalDateTime

class MedicationsListAdapter(
    private val dataList: List<MedicationIntakeModel>,
    private val medicationClick: (MedicationIntakeModel, String) -> Unit,
    private val context: Context,
) :
    RecyclerView.Adapter<MedicationsListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.medName)
        val dosage: TextView = itemView.findViewById(R.id.medDosage)
        val timeMedication: TextView = itemView.findViewById(R.id.timeMedication)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medication_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]
        holder.name.text = dataItem.name
        holder.dosage.text = buildDosageString(dataItem)
        with(holder.timeMedication) {
            //если время приема не настало, текста не будет
            if (timeHasCome(dataItem)) {
                if (dataItem.isTaken) {
                    text = buildString {
                        append(context.getString(R.string.medication_taken))
                        append(buildTimeString(dataItem.actualIntakeTime!!))
                    }
                    setTextColor(context.getColor(R.color.medication_item_text_meds_taken))
                } else {
                    text = context.getString(R.string.medication_missed)
                    setTextColor(context.getColor(R.color.medication_item_text_meds_missed))
                }
            }
        }
        holder.itemView.setOnClickListener {
            medicationClick(dataItem, buildTimeAndDosageText(dataItem))
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun timeHasCome(model: MedicationIntakeModel): Boolean {
        val currentDateTime = LocalDateTime.now()
        val intakeDateTime = LocalDateTime.of(
            currentDateTime.year,
            model.intakeDate.month,
            model.intakeDate.day,
            model.intakeTime.hour,
            model.intakeTime.minute
        )
        return currentDateTime.isEqual(intakeDateTime) || currentDateTime.isAfter(intakeDateTime)
    }


    private fun buildTimeAndDosageText(medicationIntakeModel: MedicationIntakeModel): String {
        val time = buildTimeString(medicationIntakeModel.intakeTime)
        val dosage = buildDosageString(medicationIntakeModel)
        return "$time $dosage"
    }

    private fun buildTimeString(time: MedicationIntakeModel.Time): String =
        with(time) {
            if (minute < 10) return "$hour:0$minute"
            else return "$hour:$minute"
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
            MedicationIntakeModel.IntakeType.AFTER_MEAL -> context.getString(R.string.after_meal)
            MedicationIntakeModel.IntakeType.BEFORE_MEAL -> context.getString(R.string.before_meal)
            MedicationIntakeModel.IntakeType.DURING_MEAL -> context.getString(R.string.during_meal)
            MedicationIntakeModel.IntakeType.NONE -> context.getString(R.string.empty_string)
        }
}